<div align="center">

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/showcase/promotional_graphic_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/showcase/promotional_graphic_light.png">
  <img alt="Promotional graphic with the plugin logo" src="https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/showcase/promotional_graphic_light.png">
</picture>

# intellij-discord-rp

[![Version](https://img.shields.io/jetbrains/plugin/v/24027?style=flat-square)](https://plugins.jetbrains.com/plugin/24027-discord-rich-presence)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/24027?style=flat-square)](https://plugins.jetbrains.com/plugin/24027-discord-rich-presence/reviews)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/24027?style=flat-square)](https://plugins.jetbrains.com/plugin/24027-discord-rich-presence)
[![GitHub License](https://img.shields.io/github/license/pandier/intellij-discord-rp?style=flat-square)](https://github.com/pandier/intellij-discord-rp/blob/master/LICENSE)
[![Ko-fi](https://img.shields.io/badge/Ko--fi-%23d9534f?style=flat-square&logo=ko-fi&logoColor=white)](https://ko-fi.com/pandier)

</div>

A highly customizable IntelliJ plugin that adds stylish Rich Presence support to enhance your Discord status.

## ✨ Features

- Display your work in Discord!
- Customize with variables
- Change display mode per project (or hide the project)
- Support for more than 20 languages (with more to come)
- Support for all JetBrains IDEs
- Use custom Discord application id

## 💭 The motivation

But wait, aren't there a lot of Discord integration plugins already? So why create another one?

Well, here's the thing: the existing plugins that offer Rich Presence support have either been left unmaintained
or don't provide a good enough experience that I was happy with. That's why I decided to make this plugin,
which gives the user the freedom to customize almost everything while ensuring a premium and polished feel.

## 📥 Installation

The recommended way to install this plugin is through the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/24027-discord-rich-presence),
but there are alternative ways. You can also download a distribution of the plugin in the
[release page](https://github.com/pandier/intellij-discord-rp/releases) or a build
available under every commit through GitHub actions (these builds aren't signed).

## ⚙️ Settings

The settings menu can be opened in **File -> Settings -> Discord Rich Presence**, where you can customize your Rich Presence.
Settings are split into three display modes: **Application**, **Project** and **File**. The File display mode is shown
when the user is editing a file. Otherwise, the Project mode is displayed. The Application display mode is only shown
if configured either as default display mode, or changed in a project using **Tools -> Discord Rich Presence -> Project Display Mode**
or the **Settings -> Discord Rich Presence -> Project** settings menu.

Each display mode can be configured using the corresponding tab. Variables are used for showing information in text fields, here's a list of them:

| Variable                             | File mode | Project mode | Application mode | Value                                                             |
|--------------------------------------|-----------|--------------|------------------|-------------------------------------------------------------------|
| `{app_name}`                         | ✅         | ✅            | ✅                | Name of the IDE                                                   |
| `{app_full_name}`                    | ✅         | ✅            | ✅                | Name and edition of the IDE                                       |
| `{app_version}`                      | ✅         | ✅            | ✅                | Version of the IDE                                                |
| `{project_name}`                     | ✅         | ✅            | ❌                | Name of the current project                                       |
| `{project_repo_url}` <sup>1</sup>    | ✅         | ✅            | ❌                | URL of the current project's Git repository remote                |
| `{project_repo_branch}` <sup>1</sup> | ✅         | ✅            | ❌                | Name of the current project's Git repository branch               |
| `{file_name}`                        | ✅         | ❌            | ❌                | Name of the edited file                                           |
| `{file_path}`                        | ✅         | ❌            | ❌                | Path to the edited file                                           |
| `{file_type}`                        | ✅         | ❌            | ❌                | The determined type of the edited file                            |
| `{file_dir_name}`                    | ✅         | ❌            | ❌                | Name of the directory the edited file is in                       |
| `{file_line}`                        | ✅         | ❌            | ❌                | Line number of the caret in the edited file                       |
| `{file_line_count}`                  | ✅         | ❌            | ❌                | Number of lines of the edited file                                |
| `{file_column}`                      | ✅         | ❌            | ❌                | Column number of the caret in the edited file                     |
| `{file_problems_total}`              | ✅         | ❌            | ❌                | Total number of problems (warnings and errors) in the edited file |
| `{file_problems_errors}`             | ✅         | ❌            | ❌                | Number of errors in the edited file                               |
| `{file_problems_warnings}`           | ✅         | ❌            | ❌                | Number of warnings in the edited file                             |
| `{file_size}`                        | ✅         | ❌            | ❌                | Size of the edited file                                           |

*1: Requires the [Git](https://plugins.jetbrains.com/plugin/13173-git) plugin to be installed*

## ❓ Can't see buttons in Rich Presence

There is a client-sided bug in the Discord app that makes buttons in **your own** Rich Presence invisible.
The only way you can tell is either by joining a voice channel and hovering over the channel name, or asking a friend.

## ❓ Requesting a new language

Do you want support for a language that you like?
Create a new **Icon request** issue in the [issue tracker](https://github.com/pandier/intellij-discord-rp/issues/new/choose)!

## 📜 License

This project is licensed under the [MIT License](https://github.com/pandier/intellij-discord-rp/blob/main/LICENSE).
