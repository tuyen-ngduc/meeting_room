package com.virtual_assistant.meet.dto.response;

public class MemberByIdDTO {

        private String idMember;
        private String name;

        private String role;

    public MemberByIdDTO() {
    }


        public String getIdMember() {
            return idMember;
        }

        public void setIdMember(String idMember) {
            this.idMember = idMember;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

