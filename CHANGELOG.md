# Changelog

All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Setting for specifying if elapsed time should be for the whole applcation, for each project or for each file
- `{file_size}` variable that shows the size of the edited file

### Changed

- Moved Rich Presence settings category to a new position between "Tools" and "Settings Sync"
- Removed minimum requirement of 2 characters for text fields (text is padded with spaces to fit the minimum length)

### Fixed

- Fixed Rich Presence not showing up when variables make the text too long

## [1.6.2] - 2024-12-01

### Added

- Option to show the full application name with the edition in Rich Presence title

### Changed

- Clarified "try reconnecting" setting

## [1.6.1] - 2024-11-10

### Fixed

- Connection errors on Linux occurring when the operating system is set to a non-English language

## [1.6.0] - 2024-10-30

### Added

- Ability to hide Rich Presence when IDE is out of focus for a certain amount of time (can be configured in settings)

### Changed

- Updated settings to match UI guidelines

### Fixed

- AsynchronousCloseException when closing the IDE

## [1.5.1] - 2024-10-05

### Added

- Icon for Scala

## [1.5.0] - 2024-09-08

### Added

- Icon for Database tool
- Variable `{file_line}` that shows the current line number
- Variable `{file_line_count}` that shows the number of lines in a file
- Setting ("IDE logo style") that switches between the old (classic) logo style and the new (modern) one

### Changed

- Updated IDE icons to the new modern look

### Fixed

- Plugin is now compatible for all versions since 2023.1.6

## [1.4.3] - 2024-08-21

### Added

- File type for Astro, JSX, TSX

## [1.4.2] - 2024-08-04

### Added

- File type for Sass, Cargo project files, `.env` file

### Changed

- `.ico` files are now included in the image file type
- Enable compatibility for all newly released versions of IntelliJ

## [1.4.1] - 2024-06-22

### Added

- File types for Protocol Buffer

## [1.4.0] - 2024-06-06

### Added

- File types for Ruby, F#, Shell scripts, Batch scripts, SQL, Visual Basic, Prisma schema, HTTP request files, license files

### Changed

- Redesigned icons for Java, JSON, XML, SQL, Visual Basic, Image files 
- Updated KPresence to 0.6.2

## [1.3.2] - 2024-05-21

### Fixed

- Compatibility for 242.* builds

## [1.3.1] - 2024-05-15

### Fixed

- Rich Presence doesn't update when text field is formatted with a variable that has a value of length 1 character (fixes C files not displaying)

## [1.3.0] - 2024-05-09

### Added

- File types for Svelte, Nuxt config (`nuxt.config.ts`), NPM package (`package.json`, `package-lock.json`), TypeScript config (`tsconfig.json`)

### Changed

- Move Rich Presence configuration options to root

## [1.2.1] - 2024-05-08

### Added

- Add `mjs` extension support for JavaScript

### Fixed

- `ConnectionException` on Unix machines

## [1.2.0] - 2024-05-02

### Added

- "Show elapsed time" checkbox that toggles the timestamp for a display mode
- Text field length validation
- File type for diff preview

### Changed

- Improved error handling
- KPresence is now used for back-end

### Fixed

- Emojis are now working in Rich Presence

## [1.1.1] - 2024-04-20

### Fixed

- Remove usage of experimental api

## [1.1.0] - 2024-04-20

### Added

- `{file_dir_name}` variable that displays the directory name of the edited file
- `{app_version}` variable that displays the version of the IDE
- Progress indicator and notification for reconnecting action
- New icons for Dart, images
- Improved settings user interface
- Support for remaining JetBrains IDEs
- Setting for changing display mode per project (Tools -> Discord Rich Presence -> Change Display Mode in Project)

  - The default value can be configured in global settings
- Added application display mode

### Changed

- `{file_path}` now uses path relative to content root
- `{app_full_name}` now uses edition instead of version
- Apply settings reconnecting only when custom application id is modified
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

[Unreleased]: https://github.com/pandier/intellij-discord-rp/compare/v1.6.2...HEAD
[1.6.2]: https://github.com/pandier/intellij-discord-rp/compare/v1.6.1...v1.6.2
[1.6.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.6.0...v1.6.1
[1.6.0]: https://github.com/pandier/intellij-discord-rp/compare/v1.5.1...v1.6.0
[1.5.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.5.0...v1.5.1
[1.5.0]: https://github.com/pandier/intellij-discord-rp/compare/v1.4.3...v1.5.0
[1.4.3]: https://github.com/pandier/intellij-discord-rp/compare/v1.4.2...v1.4.3
[1.4.2]: https://github.com/pandier/intellij-discord-rp/compare/v1.4.1...v1.4.2
[1.4.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.4.0...v1.4.1
[1.4.0]: https://github.com/pandier/intellij-discord-rp/compare/v1.3.2...v1.4.0
[1.3.2]: https://github.com/pandier/intellij-discord-rp/compare/v1.3.1...v1.3.2
[1.3.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/pandier/intellij-discord-rp/compare/v1.2.1...v1.3.0
[1.2.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/pandier/intellij-discord-rp/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/pandier/intellij-discord-rp/compare/v1.0.2...v1.1.0
[1.0.2]: https://github.com/pandier/intellij-discord-rp/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/pandier/intellij-discord-rp/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/pandier/intellij-discord-rp/commits/v1.0.0
