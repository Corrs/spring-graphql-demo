package com.yanxuan88.australiacallcenter.event.listener;

public abstract class NoticeMessage {
    private Long userId;

    public NoticeMessage(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    /**
     * 去哪个channel
     *
     * @return channel name
     */
    protected abstract String toChannel();
}
