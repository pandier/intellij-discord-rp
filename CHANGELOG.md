# Changelog

All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Improved settings user interface
- Support for remaining JetBrains IDEs
- Setting for changing display mode per project (Tools -> Discord Rich Presence -> Change Display Mode in Project)

  - The default value can be configured in global settings

- Added application display mode

### Changed

- Minimum supported IntelliJ version is now 2023.1
- Display mode settings are now grouped into tabs
- All actions are now dumb aware

### Fixed

- Blank fields causing the activity to not show up

## [1.0.2] - 2024-03-29

### Fixed

- Focus change listener not disposing correctly - causing error on shutdown

## [1.0.1] - 2024-03-27

### Fixed

- Large image checkbox setting in Project display mode disables itself

## [1.0.0] - 2024-03-24

Initial release

[Unreleased]: https://github.com/pandier/intellij-discord-rp/compare/v1.0.2...HEAD
[1.0.2]: https://github.com/pandier/intellij-discord-rp/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/pandier/intellij-discord-rp/commits/v1.0.0
