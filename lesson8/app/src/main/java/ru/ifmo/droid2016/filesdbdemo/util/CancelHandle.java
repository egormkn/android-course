package ru.ifmo.droid2016.filesdbdemo.util;

/**
 * Интерфейс для пассивной отмены долго-работающих тасков.
 * Таск, получивший экземпляр CancelHandle должен в процессе работы регулярно вызывать
 * метод isCanceled и немедленно завершать работу, если он вернул true.
 */
public interface CancelHandle {

    boolean isCanceled();
}
