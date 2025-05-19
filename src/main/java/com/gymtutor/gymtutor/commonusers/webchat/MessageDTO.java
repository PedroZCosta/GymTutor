package com.gymtutor.gymtutor.commonusers.webchat;

public class MessageDTO {
    private Long id;
    private String content;
    private UserDTO sender;
    private UserDTO receiver;

    // construtor
    public MessageDTO() {
        // construtor padrão exigido pelo JSON
    }
    public MessageDTO(MessageModel message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sender = new UserDTO(message.getSender());
        this.receiver = new UserDTO(message.getReceiver());
    }

    // getters e setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public UserDTO getSender() {return sender;}
    public void setSender(UserDTO sender) {this.sender = sender;}
    public UserDTO getReceiver() {return receiver;}
    public void setReceiver(UserDTO receiver) {this.receiver = receiver;}
}