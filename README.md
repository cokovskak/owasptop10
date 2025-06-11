```git clone https://github.com/cokovskak/owasptop10.git```

#A01:2021 - Broken Access Control
##Improper checks in the controller allow users to view resources they should not access (e.g., student grades via URL manipulation).

#A02:2021 - Cryptographic Failures
##Passwords are stored using weak Base64 encoding instead of secure hashing algorithms like BCrypt or Argon2.

#A03:2021 - Injection
##SQL Injection vulnerabilities are demonstrated using poorly concatenated queries in a feedback form and search functionality.

#A04:2021 - Insecure Design
##Hardcoded database credentials are stored in version control due to missing .gitignore entries. This poses serious risks if made public.

#A05:2021 - Security Misconfiguration
##Error stack traces are shown to users, leaking sensitive information such as framework types, Java versions, and more.

#A06:2021 - Vulnerable and Outdated Components
##The application uses an outdated version of Log4j, exposing it to the Log4Shell vulnerability (CVE-2021-44228).

#A07:2021 - Identification and Authentication Failures
##Weak reset mechanisms based on security questions and poor credential requirements leave the system vulnerable to brute-force and OSINT attacks.

#A08:2021 - Software and Data Integrity Failures
##Uploaded files are not validated and are executed on the server, allowing execution of potentially malicious code (e.g., .py scripts).

#A09:2021 - Security Logging and Monitoring Failures
##Login attempts log both usernames and passwords in plaintext, risking credential leaks if log files are accessed.

#A10:2021 - Server-Side Request Forgery (SSRF)
##An endpoint fetches external resources without validation, which attackers can exploit to access local or internal services and files.

