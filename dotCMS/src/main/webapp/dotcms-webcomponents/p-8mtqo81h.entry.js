import{r as t}from"./p-17c4ed33.js";import"./p-f1ae637c.js";import{c as r,d as s}from"./p-0ff2a75e.js";const e=class{constructor(r){t(this,r)}render(){return this.column.fields.map(t=>this.getField(t))}getField(t){return r(t,this.fieldsToShow)?this.getFieldTag(t):null}getFieldTag(t){return s[t.fieldType]?s[t.fieldType](t):""}static get style(){return"dot-form-column{-ms-flex:1;flex:1;margin:1rem}dot-form-column:first-child{margin-left:0}dot-form-column:last-child{margin-right:0}"}};export{e as dot_form_column};