import{r as s,h as t}from"./p-17c4ed33.js";import{o as l}from"./p-f1ae637c.js";const a=class{constructor(t){s(this,t),this.name="",this.label="",this.required=!1}render(){return t("label",{class:"dot-label",id:l(this.name)},t("span",{class:"dot-label__text"},this.label,this.required?t("span",{class:"dot-label__required-mark"},"*"):null),t("slot",null))}static get style(){return".dot-field__error-message,.dot-field__hint{display:block;font-size:.75rem;line-height:1rem;margin-top:.25rem;position:absolute;-webkit-transition:opacity .2s ease;transition:opacity .2s ease}.dot-field__error-message{color:red;opacity:0}.dot-invalid.dot-dirty>.dot-field__hint{opacity:0}.dot-invalid.dot-dirty>.dot-field__error-message{color:red;opacity:1}dot-label>label{display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column}dot-label>label .dot-label__text{line-height:1.25rem;margin-bottom:.25rem}"}};export{a as dot_label};