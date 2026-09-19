#!/usr/bin/env python3
"""
Send a one-line summary of a petwatch run to a webhook.

Reads the JSON that `petwatch.py --json` writes and POSTs a short message to a
URL that accepts JSON - Twilio, CallMeBot, ntfy, a Zapier/Make hook, and so on.
The payload carries the same text under several common keys so it suits more
services without needing a template per provider.

    python tools/petwatch/notify.py --findings findings.json --url "$WEBHOOK"
    python tools/petwatch/notify.py --findings findings.json --dry-run

The URL may also come from PETWATCH_WEBHOOK. Exits 0 on success, 2 when there is
nothing worth sending, and 1 on failure.
"""

from __future__ import annotations

import argparse
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path

TIMEOUT = 30


def summarise(findings: dict) -> str:
    """One line describing what the run turned up, or '' if nothing did."""
    parts = []
    counts = (
        ("missing", "pet variant(s) to add"),
        ("rate_changes", "drop rate(s) changed"),
        ("rate_conflicts", "drop rate(s) disagreeing with the plugin"),
        ("new_pages", "new pet page(s)"),
        ("fetch_failures", "page(s) unreadable"),
    )
    for key, label in counts:
        n = len(findings.get(key) or [])
        if n:
            parts.append(str(n) + " " + label)
    if not parts:
        return ""
    return "PetInfoPlugin: " + ", ".join(parts) + ". See the new issue."


def post(url: str, text: str) -> None:
    # urlopen also opens file: and ftp: URLs; a webhook is only ever http(s)
    if urllib.parse.urlsplit(url).scheme not in ("http", "https"):
        raise ValueError("webhook URL must be http or https")
    # different services read different keys; send the common ones
    payload = {"text": text, "message": text, "content": text, "body": text}
    request = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json", "User-Agent": "PetInfoPlugin-petwatch/1.0"},
        method="POST",
    )
    with urllib.request.urlopen(request, timeout=TIMEOUT) as response:
        response.read()


def main() -> int:
    parser = argparse.ArgumentParser(description="Send a petwatch summary to a webhook.")
    parser.add_argument("--findings", type=Path, required=True, help="JSON written by petwatch --json")
    parser.add_argument("--url", default=os.environ.get("PETWATCH_WEBHOOK", ""),
                        help="webhook URL (default: $PETWATCH_WEBHOOK)")
    parser.add_argument("--dry-run", action="store_true", help="print the message instead of sending it")
    args = parser.parse_args()

    if not args.findings.exists():
        print("notify: no findings file at " + str(args.findings), file=sys.stderr)
        return 1
    try:
        findings = json.loads(args.findings.read_text(encoding="utf-8"))
    except ValueError as exc:
        print("notify: " + str(args.findings) + " is not valid JSON: " + str(exc), file=sys.stderr)
        return 1

    text = summarise(findings)
    if not text:
        print("notify: nothing to report", file=sys.stderr)
        return 2

    if args.dry_run:
        print(text)
        return 0

    if not args.url:
        print("notify: no webhook URL given and PETWATCH_WEBHOOK is unset", file=sys.stderr)
        return 1

    try:
        post(args.url, text)
    except (urllib.error.URLError, TimeoutError, OSError, ValueError) as exc:
        # the run itself succeeded; say so loudly but let the caller decide
        print("notify: POST failed: " + str(exc), file=sys.stderr)
        return 1
    print("notify: sent")
    return 0


if __name__ == "__main__":
    sys.exit(main())
