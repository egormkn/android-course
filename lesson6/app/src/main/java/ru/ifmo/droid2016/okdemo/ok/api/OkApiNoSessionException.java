package ru.ifmo.droid2016.okdemo.ok.api;

/**
 * Исключение, говорящее о том, что у нас нет сессии для выполнения запроса к Ok API
 * (грубо говоря -- пользователь не залогинился).
 */
public class OkApiNoSessionException extends ApiException {

    private static final long serialVersionUID = 1L;

    public OkApiNoSessionException() {
        super("Has no session for performing API request");
    }

    public OkApiNoSessionException(String detailMessage) {
        super(detailMessage);
    }
}
