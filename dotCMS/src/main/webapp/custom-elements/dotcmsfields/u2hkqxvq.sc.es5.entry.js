dotcmsFields.loadBundle("u2hkqxvq",["exports","./chunk-35cb6fec.js","./chunk-ce967dd2.js"],function(t,e,n){var i=window.dotcmsFields.h,a=function(){function t(){this.value="",this.name="",this.required=!1,this.disabled=!1,this.min="",this.max="",this.step="1",this.type=""}return t.prototype.reset=function(){this.value="",this.status=n.getOriginalStatus(this.isValid()),this.emitValueChange(),this.emitStatusChange()},t.prototype.componentWillLoad=function(){this.status=n.getOriginalStatus(this.isValid()),this.emitStatusChange()},t.prototype.render=function(){var t=this;return i(e.Fragment,null,i("input",{class:n.getErrorClass(this.status.dotValid),disabled:this.disabled||null,id:n.getId(this.name),onBlur:function(){return t.blurHandler()},onInput:function(e){return t.setValue(e)},required:this.required||null,type:this.type,value:this.value,min:this.min,max:this.max,step:this.step}))},t.prototype.isValid=function(){return this.isValueInRange()&&this.isRequired()},t.prototype.isRequired=function(){return!this.required||!!this.value},t.prototype.isValueInRange=function(){return this.isInMaxRange()&&this.isInMinRange()},t.prototype.isInMinRange=function(){return!this.min||this.value>=this.min},t.prototype.isInMaxRange=function(){return!this.max||this.value<=this.max},t.prototype.blurHandler=function(){this.status.dotTouched||(this.status=n.updateStatus(this.status,{dotTouched:!0}),this.emitStatusChange())},t.prototype.setValue=function(t){this.value=t.target.value.toString(),this.status=n.updateStatus(this.status,{dotTouched:!0,dotPristine:!1,dotValid:this.isValid()}),this.emitValueChange(),this.emitStatusChange()},t.prototype.emitStatusChange=function(){this._statusChange.emit({name:this.name,status:this.status,isValidRange:this.isValueInRange()})},t.prototype.emitValueChange=function(){this._valueChange.emit({name:this.name,value:this.formattedValue()})},t.prototype.formattedValue=function(){return 5===this.value.length?this.value+":00":this.value},Object.defineProperty(t,"is",{get:function(){return"dot-input-calendar"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"properties",{get:function(){return{disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},max:{type:String,attr:"max",reflectToAttr:!0},min:{type:String,attr:"min",reflectToAttr:!0},name:{type:String,attr:"name",reflectToAttr:!0},required:{type:Boolean,attr:"required",reflectToAttr:!0},reset:{method:!0},status:{state:!0},step:{type:String,attr:"step",reflectToAttr:!0},type:{type:String,attr:"type",reflectToAttr:!0},value:{type:String,attr:"value",reflectToAttr:!0,mutable:!0}}},enumerable:!0,configurable:!0}),Object.defineProperty(t,"events",{get:function(){return[{name:"_valueChange",method:"_valueChange",bubbles:!0,cancelable:!0,composed:!0},{name:"_statusChange",method:"_statusChange",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(t,"style",{get:function(){return"dot-input-calendar{display:-ms-flexbox;display:flex}dot-input-calendar input{-ms-flex-positive:1;flex-grow:1}"},enumerable:!0,configurable:!0}),t}(),r=function(){function t(){this.name="",this.label="",this.required=!1}return t.prototype.render=function(){return i("label",{class:"dot-label",id:n.getLabelId(this.name)},i("span",{class:"dot-label__text"},this.label,this.required?i("span",{class:"dot-label__required-mark"},"*"):null),i("slot",null))},Object.defineProperty(t,"is",{get:function(){return"dot-label"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"properties",{get:function(){return{label:{type:String,attr:"label",reflectToAttr:!0},name:{type:String,attr:"name",reflectToAttr:!0},required:{type:Boolean,attr:"required",reflectToAttr:!0}}},enumerable:!0,configurable:!0}),Object.defineProperty(t,"style",{get:function(){return".dot-field__error-message,.dot-field__hint{display:block;font-size:.75rem;margin-top:.25rem;position:absolute;-webkit-transition:opacity .2s ease;transition:opacity .2s ease}.dot-field__error-message{color:red;opacity:0}.dot-invalid.dot-dirty>.dot-field__hint{opacity:0}.dot-invalid.dot-dirty>.dot-field__error-message{color:red;opacity:1}dot-label>label{display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column}dot-label>label .dot-label__text{margin-bottom:.25rem}"},enumerable:!0,configurable:!0}),t}();t.DotInputCalendar=a,t.DotLabel=r,Object.defineProperty(t,"__esModule",{value:!0})});