package com.shop.ecommerce.entity;

import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

    @MappedSuperclass
    public class Tracking {
        @CreationTimestamp
        private String created_at;
        @UpdateTimestamp
        private String updated_at;

        private boolean is_deleted;
        private boolean is_archived;

        private String created_by="Admin";
        private String updated_by="Admin";

        public Tracking() {}
        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public boolean isIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(boolean is_deleted) {
            this.is_deleted = is_deleted;
        }

        public boolean isIs_archived() {
            return is_archived;
        }

        public void setIs_archived(boolean is_archived) {
            this.is_archived = is_archived;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        public Tracking(String created_at, String updated_at, boolean is_deleted, String created_by, String updated_by) {
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.is_deleted = is_deleted;
            this.created_by = created_by;
            this.updated_by = updated_by;
        }
    }


