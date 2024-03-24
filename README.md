<h1>
  <img alt="logo" width="32px" src="https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/src/main/resources/META-INF/pluginIcon.svg" />
  intellij-discord-rp
</h1>

A highly customizable IntelliJ plugin that adds stylish Rich Presence support to enhance your Discord status.

## ✨ Features

- Display your work in Discord!
- Customize with variables
- Hide projects that you don't want others to see
- Support for more than 20 languages (with more to come)
- Support for 7 JetBrains IDEs (IntelliJ Idea, PyCharm, PhpStorm, WebStorm, CLion, GoLand, Rider)
- Use custom Discord application id

## 👀 Showcase

<img width="777px" alt="showcase" src="https://raw.githubusercontent.com/pandier/intellij-discord-rp/main/showcase/collage.png" />

## 💭 The motivation

But wait, aren't there a lot of Discord integration plugins already? So why create another one?

Well, here's the thing: the existing plugins that offer Rich Presence support have either been left unmaintained
or don't provide a good enough experience that I was happy with. That's why I decided to make this plugin,
which gives the user the freedom to customize almost everything while ensuring a premium and polished feel.

## 📥 Installation

The recommended way to install this plugin is through the JetBrains Marketplace,
but there are alternative ways. You can also download a distribution of the plugin in the
[release page](https://github.com/pandier/intellij-discord-rp/releases) or a build
available under every commit through GitHub actions (these builds aren't signed).

## ⚙️ Settings

The settings menu can be opened in **Settings -> Tools -> Discord Rich Presence**, where you can customize your Rich Presence.
Settings are split into two display modes: **Project** and **File**. The File display mode is shown
when the user is editing a file. Otherwise, the Project mode is displayed.
We use variables for showing information in text fields, here's a list of them:

| Variable          | File mode | Project mode | Value                                   |
|-------------------|-----------|--------------|-----------------------------------------|
| `{app_name}`      | ✅         | ✅            | Name of the IDE                         |
| `{app_full_name}` | ✅         | ✅            | Name and version of the IDE             |
| `{project_name}`  | ✅         | ✅            | Name of the project                     |
| `{file_name}`     | ✅         | ❌            | Name of the current file                |
| `{file_path}`     | ✅         | ❌            | Path to the current file                |
| `{file_type}`     | ✅         | ❌            | The determined type of the current file |

## ❓ Requesting a new language

Do you want support for a language that you like?
Create a new **Language request** issue in the [issue tracker](https://github.com/pandier/intellij-discord-rp/issues/new/choose)!

## 📜 License

This project is licensed under the [MIT License](https://github.com/pandier/intellij-discord-rp/blob/main/LICENSE).
