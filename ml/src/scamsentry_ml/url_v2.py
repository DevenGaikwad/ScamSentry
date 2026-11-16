"""Bounded lexical inspection; no network, DNS, redirect or reputation lookup."""
import ipaddress
import re
from .legacy_v1 import URL_PATTERN
VERSION='lexical_v2'
MAX_LINKS=40

def inspect_one(original):
    codes=[]
    try:
        candidate=original if '://' in original else 'https://'+original
        scheme,rest=candidate.split('://',1)
        scheme=scheme.lower()
        if scheme not in ('http','https') or any(ord(c)<33 or c in '\\\u200b\u200c\u200d\ufeff' for c in candidate): raise ValueError('syntax')
        authority=re.split(r'[/\?#]',rest,1)[0]
        if authority.count('@')>1: raise ValueError('multiple userinfo')
        if '@' in authority: codes.append('userinfo')
        hostport=authority.rsplit('@',1)[-1]
        port=None
        if hostport.startswith('['):
            m=re.fullmatch(r'\[([0-9a-fA-F:]+)\](?::([0-9]+))?',hostport)
            if not m: raise ValueError('IPv6 syntax')
            host=m[1].lower(); ipaddress.IPv6Address(host); port=m[2]
        else:
            if hostport.count(':')>1: raise ValueError('unbracketed IPv6')
            parts=hostport.split(':'); raw=parts[0].rstrip('.')
            if len(parts)==2:
                port=parts[1]
                if not port or not re.fullmatch('[0-9]+',port): raise ValueError('port')
            # Reject IDNA deviation characters instead of silently mapping them differently across runtimes.
            if any(c in raw for c in '%ßςẞ'): raise ValueError('ambiguous IDNA')
            host=raw.encode('idna').decode('ascii').lower()
            labels=host.split('.')
            if not host or len(host)>253 or any(not re.fullmatch(r'[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?',label) for label in labels): raise ValueError('host')
            if re.fullmatch('[0-9.]+',host): ipaddress.IPv4Address(host)
            elif re.fullmatch(r'(?:[0-9]+|0x[0-9a-f]+)',labels[-1]): raise ValueError('ambiguous numeric host')
        if port is not None and not 1<=int(port)<=65535: raise ValueError('port')
        if 'xn--' in host: codes.append('idn')
        if scheme=='http': codes.append('unencrypted_scheme')
        if host in ('bit.ly','tinyurl.com','t.co'): codes.append('shortener')
        if len(original)>180: codes.append('long_address')
        return dict(original=original,host=host,codes=codes,valid=True)
    except (ValueError,UnicodeError):
        return dict(original=original,host=None,codes=['unsupported_or_malformed'],valid=False)

def extract(text):
    addresses=[]
    for m in URL_PATTERN.finditer(text):
        if len(addresses)==MAX_LINKS: return addresses,True
        original=m.group().rstrip('.,;!)}')
        while original.endswith(']') and original.count(']')>original.count('['):original=original[:-1]
        addresses.append(inspect_one(original))
    return addresses,False
