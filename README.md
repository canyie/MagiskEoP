## Introduction
This is an exploit for a vulnerability in Magisk app that allows local apps to silently gain root access without user consent. 
Vulnerability was initially reported by [@vvb2060](https://github.com/vvb2060) and PoC-ed by [@canyie](https://github.com/canyie). It has been fixed in Canary 27007.
Demo video for exploit this vulnerability to silently obtaining root privileges and granting root to any app: https://github.com/canyie/MagiskEoP/blob/main/screen-20220302-093745.mp4


Steps to reproduce this vulnerability:
1. Install vulnerable Magisk app builds on a device that has no GMS preinstalled
2. Install this exploit app
3. Force stop Magisk app and this exploit app
4. Open Magisk app
5. Open this exploit app, type your commands and press Execute to execute them with root privileges

## Vulnerability Info
Name: Magisk App Arbitrary Code Execution Vulnerability
Alias: Magisk Privilege Escalation Vulnerability

### The Basics
Product: Magisk
CVE: N/A (not yet assigned)
Reporter: [@vvb2060](https://github.com/vvb2060)
Initial Report Date: 2024-08-01
Patch Date: 2024-08-21
Disclosure Date: 2024-08-24
Affected Versions: Manager v7.0.0 ~ Canary 27006
First Patched Versions: Canary 27007
Issue/Bug report: https://github.com/topjohnwu/Magisk/issues/8279
Patch CL: https://github.com/topjohnwu/Magisk/commit/c2eb6039579b8a2fb1e11a753cea7662c07bec02
Bug-introducing CL: https://github.com/topjohnwu/Magisk/commit/920b60da19212dd8d54d27ada77a30067ce50de6
Bug Class: Unsafe Dynamic External Code Loading
Weakness Enumerations:
- [CWE-386: Symbolic Name not Mapping to Correct Object](https://cwe.mitre.org/data/definitions/386.html)
- [CWE-829: Inclusion of Functionality from Untrusted Control Sphere](https://cwe.mitre.org/data/definitions/829.html)

### Summary
Magisk is a suite of open source software for customizing Android. Prior to version canary 27007, in install of ProviderInstaller.java, there is a possible way to load arbitrary code into Magisk app due to a missing package validation. This could lead to local escalation of privileges allowing attackers to gain root access with no additional privileges needed. User interaction is not needed for exploitation.

### Details
Old Android versions do not support some algorithms. To make Magisk work properly on these platforms, it tries to load conscrypt from GMS by calling [createCallingContext()](https://developer.android.com/reference/android/content/Context#createPackageContext(java.lang.String,%20int)). Check this link for more details: https://t.me/vvb2060Channel/692

However, GMS is not always preinstalled on all devices. Magisk assumes that loading code from GMS is always safe, however attackers can create a fake malicious app with the same package name. When Magisk app is launched, malicious code will get executed in Magisk app. Since Magisk app is always granted root access, this allows attackers to silently gain root access and execute arbitrary code with root privileges without user acceptance.

### Vulnerable Devices
- Devices with no GMS preinstalled
- Devices with broken signature verification implementation (e.g. Disabled by CorePatch)

Note: This issue is fixed in Canary 27007 by ensuring GMS is a system app before loading it. However, it's still possible to exploit this issue on devices with pre-installed GMS but have broken signature verification implementations (e.g. CorePatch).

