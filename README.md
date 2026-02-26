# 🎤 Slip of a Tongue: Whisper Edition

**Slip of a Tongue: Whisper Edition** allows players to trigger server-side commands using their actual voice. Powered by the high-accuracy **WhisperLib** engine, your spoken words are processed locally and sent to the server for secure execution.

***

## 🔥 What's New in Whisper Edition?

1.  **High Accuracy**: Powered by OpenAI’s Whisper, it understands natural speech, accents, and fast talkers far better than the old VoskLib.

2.  **Smart Noise Filtering**: Automatically ignores background music, coughs, and non-speech sounds like `[MUSIC]` or `*laughing*`.

3.  **Zero Lag**: Audio processing happens on a separate thread, so your FPS stays buttery smooth.


***

## 🛠 Installation

### Client-Side (Required)

1.  Install WhisperLib.

2.  Install **Slip of a Tongue: Whisper Edition**.

3.  **Download a Model**: Open the WhisperLib config in-game and download a model (we recommend `base.en` for the best performance-to-speed ratio).


### Server-Side (Required)

1.  Install **Slip of a Tongue: Whisper Edition** (WhisperLib is **not** required on the server).

2.  Configure your word list in the server's config folder.


***

## ⚙️ Command Syntax

The server-side configuration uses a specific format to map voice triggers:

`word:[command1, command2, %player%]Text to narrate`

*   **word**: The keyword or phrase to detect.
*   **\[...\]**: List of commands (no `/` needed).
*   **%player%**: Automatically replaced by the speaker's name.
*   **Text to narrate**: The message sent to chat upon success.

> **Example:** `jump:[effect give %player% levitation 2 10]See you later!`

***

## 🔒 Security & Privacy

*   **Local Processing**: Speech-to-text happens entirely on your machine. No audio data ever leaves your computer.
*   **Server-Side Validation**: The server only executes commands that are explicitly defined in its configuration, preventing players from spoofing unauthorized commands.

***

## 📜 Credits

*   **Engine**: WhisperLib by MeroSsSany.
*   **Original Inspiration**: [VoskLib](https://www.curseforge.com/minecraft/mc-mods/vosklib) and [Slip of a Tongue](https://www.curseforge.com/minecraft/mc-mods/slip-of-a-tongue).
*   **Speech Model**: Powered by OpenAI's Whisper.