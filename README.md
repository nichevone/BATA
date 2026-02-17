# BATA
The Basic Audio Transmitting Application.\
A voice chat within the LAN, written in pure java.

## Why?
This project was made as an experiment. So don't take it seriously! :)\
I was needed the tool to talk with my brother in the other room. Using heavy programs wasn't the solution I wanted, so I created my own voice chat.
#### Here's some pros that moved me to make this:
- Lightweight: uses under 1Mb disk space and around 60 Mb RAM
- Easy-to-use: no unnecessary things like account, just a chat
- No internet dependency: works within a LAN

## Drawbacks
 - You can't choose what microphone or what speakers to use
 - It only supports one-to-one connection, meaning there's no group voice chat
 - Still has some bugs with connections

## Architecture
Connection is made using UDP. Currently, for sending and receiving signal DatagramSocket and DatagramPacket classes are used. And the DataLine classes are used to process audio. For GUI swing was used.

## Roadmap
- [x] GUI
- [ ] Mute button
- [ ] Input gain changing
- [ ] Ability to choose I/O devices
- [ ] Different localizations
- [ ] Text Chat
- [ ] Multi-user connection