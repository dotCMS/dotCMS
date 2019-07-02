const t=window.dotcmsFields.h;import{a as e}from"./chunk-1d89c98b.js";import{b as a,d as s,e as i,h as r,i as l}from"./chunk-098a701f.js";class n{constructor(){this.value="",this.name="",this.label="",this.hint="",this.required=!1,this.requiredMessage="This field is required",this.validationMessage="The field doesn't comply with the specified format",this.disabled=!1,this.min="",this.max="",this.step="1"}reset(){this.el.querySelector("dot-input-calendar").reset()}componentWillLoad(){this.validateProps()}minWatch(){this.min=a(this,"min","date")}maxWatch(){this.max=a(this,"max","date")}emitValueChange(t){t.stopImmediatePropagation();const e=t.detail;this.value=e.value,this.valueChange.emit(e)}emitStatusChange(t){t.stopImmediatePropagation();const e=t.detail;this.classNames=s(e.status,e.status.dotValid,this.required),this.setErrorMessageElement(e),this.statusChange.emit({name:e.name,status:e.status})}hostData(){return{class:this.classNames}}render(){return t(e,null,t("dot-label",{label:this.label,required:this.required,name:this.name},t("dot-input-calendar",{"aria-describedby":i(this.hint),tabIndex:this.hint?0:null,disabled:this.disabled,type:"date",name:this.name,value:this.value,required:this.required,min:this.min,max:this.max,step:this.step})),r(this.hint),this.errorMessageElement)}validateProps(){this.minWatch(),this.maxWatch()}setErrorMessageElement(t){this.errorMessageElement=l(!t.status.dotValid&&!t.status.dotPristine,this.getErrorMessage(t))}getErrorMessage(t){return this.value?t.isValidRange?"":this.validationMessage:this.requiredMessage}static get is(){return"dot-date"}static get properties(){return{classNames:{state:!0},disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},errorMessageElement:{state:!0},hint:{type:String,attr:"hint",reflectToAttr:!0},label:{type:String,attr:"label",reflectToAttr:!0},max:{type:String,attr:"max",reflectToAttr:!0,mutable:!0,watchCallbacks:["maxWatch"]},min:{type:String,attr:"min",reflectToAttr:!0,mutable:!0,watchCallbacks:["minWatch"]},name:{type:String,attr:"name",reflectToAttr:!0},required:{type:Boolean,attr:"required",reflectToAttr:!0},requiredMessage:{type:String,attr:"required-message",reflectToAttr:!0},reset:{method:!0},step:{type:String,attr:"step",reflectToAttr:!0},validationMessage:{type:String,attr:"validation-message",reflectToAttr:!0},value:{type:String,attr:"value",reflectToAttr:!0,mutable:!0}}}static get events(){return[{name:"valueChange",method:"valueChange",bubbles:!0,cancelable:!0,composed:!0},{name:"statusChange",method:"statusChange",bubbles:!0,cancelable:!0,composed:!0}]}static get listeners(){return[{name:"_valueChange",method:"emitValueChange"},{name:"_statusChange",method:"emitStatusChange"}]}static get style(){return""}}export{n as DotDate};