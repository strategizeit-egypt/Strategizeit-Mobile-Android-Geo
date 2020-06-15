package com.xapps.karbala.model.data.dto.generic;

import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.model.data.dto.ErrorDTO;
import com.xapps.karbala.model.data.dto.MetaDTO;


public class ObjectModel<T>{
        @SerializedName("model")
        private T model;
        @SerializedName("metas")
        private MetaDTO metas;
        @SerializedName("errors")
        private ErrorDTO errors;

        public ObjectModel(T model, MetaDTO metas, ErrorDTO errors) {
            this.model = model;
            this.metas = metas;
            this.errors = errors;
        }

        public T getModel() {
            return model;
        }
        public void setModel(T model) {
            this.model = model;
        }

        public MetaDTO getMetas() {
            return metas;
        }
        public void setMetas(MetaDTO metas) {
            this.metas = metas;
        }

        public ErrorDTO getErrors() {
            return errors;
        }
        public void setErrors(ErrorDTO errors) {
            this.errors = errors;
        }


}

