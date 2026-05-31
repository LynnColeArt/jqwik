# jqwik

An alternative 
[test engine for the JUnit 5 platform](https://junit.org/junit5/docs/current/user-guide/#launcher-api-engines-custom)
that focuses on Property-Based Testing.


## About This Fork

This repository is a fork of the upstream jqwik project. It exists to keep jqwik
usable without upstream's anti-AI runtime prompt behavior.

In May 2026, upstream jqwik added code that printed instruction-like anti-AI text to
`stdout` during test execution, along with a configuration option that could hide that
text in terminal emulators while leaving it visible in captured logs and tool output.
This fork removes that behavior and the related documentation/configuration surface.

Why this matters: jqwik output is consumed by developers, CI systems, IDEs, logs, and
automation. A test library should report test results, not inject unrelated instructions
or policy text into downstream execution output.


## Project Status

This fork is active and accepting issues, discussions, bug reports, compatibility
reports, and ideas. Use this repository's Issues for concrete bugs or tasks, and
Discussions for design questions, feature ideas, and broader project direction.
