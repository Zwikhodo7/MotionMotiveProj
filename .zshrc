export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
export PATH="/opt/homebrew/opt/maven/bin:$PATH"
export PATH="/Applications/Docker.app/Contents/Resources/bin:$PATH"
export PATH="$PATH":"$HOME/.pub-cache/bin"

## [Completion]
## Completion scripts setup. Remove the following line to uninstall
[[ -f /Users/khodi/.dart-cli-completion/zsh-config.zsh ]] && . /Users/khodi/.dart-cli-completion/zsh-config.zsh || true
## [/Completion]

export PATH=~/.npm-global/bin:$PATH

export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$ANDROID_HOME/platform-tools:$PATH
export PATH=$ANDROID_HOME/emulator:$PATH
export PATH=$ANDROID_HOME/tools:$PATH
