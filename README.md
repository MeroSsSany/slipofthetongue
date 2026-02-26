<h1 style="text-align: center;">Slip of a Tongue</h1><p style="text-align: center;">Voice-controlled command execution for Minecraft.</p>

**Slip of a Tongue** allows players to trigger server-side commands and custom narrations using their actual voice. By leveraging the **VoskLib** client-side speech recognition engine, your spoken words are translated to text locally and sent to the server for secure validation and execution.

***

## 🎤 How it Works

1.  **The Client:** Uses **VoskLib** (locally) to turn your microphone input into a string of text.
2.  **The Bridge:** The client sends this string to the server.
3.  **The Server:** Validates the word against a pre-defined list. If it matches, the server executes the linked commands and narrations.

***

## 🛠 Installation

### Client-Side (Required)

1.  Install [VoskLib](https://www.curseforge.com/minecraft/mc-mods/vosklib).
2.  Install **Slip of a Tongue**.
3.  **Important:** Open the VoskLib config in-game _(by clicking Mods -> VoskLib -> Config)_ and download a language model for the speech recognition to work.
4.  The voice recognition will be automatically enabled upon joining a world.

### Server-Side (Required)

1.  Install **Slip of a Tongue** (VoskLib is **not** required on the server).
2.  Configure your word list in the server's config folder.

***

## ⚙️ Command Syntax

The server-side configuration uses a specific format to map voice triggers:

`word:[command1, command2, command3 %player%]text to narrate`

### Breakdown:

*   **`word`**: The keyword detected by the client's STT.
*   **`[...]`**: A list of server commands (no `/` needed).
*   **`%player%`**: Replaced by the name of the player who spoke.
*   **`text to narrate`**: The message that will be narrated/sent back upon success.

### Example Entry:

> `ascend:[effect give %player% levitation 5 1, say %player% is ascending!]Up we go!`

***

## 🔒 Security & Privacy

*   **Client-Side STT:** Speech processing is handled entirely on your computer via VoskLib. No audio data is ever sent to the server or the cloud.
*   **Server-Side Validation:** To prevent players from spoofing commands, the server only executes actions for words explicitly defined in the server-side configuration file.

***

## 📜 Credits

*   **VoskLib:** Created by [InfinityTwo](https://www.curseforge.com/members/infinitytwo/projects)
*   **Speech Engine:** Powered by [Vosk](https://alphacephei.com/vosk/).
*   **License:** GNU General Public License v3.0