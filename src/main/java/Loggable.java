public interface Loggable {
    char senderType = 'S';
    char receiverType = 'R';
    char handlerType = 'H';
    char exceptType = 'E';

    void log(char type, String message);
    void setLogger(InfoLogger logger);
}
