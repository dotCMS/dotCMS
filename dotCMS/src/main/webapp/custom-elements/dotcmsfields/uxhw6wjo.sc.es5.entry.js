dotcmsFields.loadBundle("uxhw6wjo",["exports","./chunk-a594e46d.js"],function(e,t){var r=window.dotcmsFields.h,i=function(e){return{key:e.label,value:e.value}},a=function(){function e(){this.disabled=!1,this.hint="",this.label="",this.name="",this.required=!1,this.requiredMessage="This field is required",this.value="",this.fieldType="",this.items=[]}return e.prototype.valueWatch=function(){this.value=t.checkProp(this,"value","string"),this.items=t.getDotOptionsFromFieldValue(this.value).map(i)},e.prototype.reset=function(){this.items=[],this.status=t.getOriginalStatus(this.isValid()),this.emitChanges()},e.prototype.deleteItemHandler=function(e){e.stopImmediatePropagation(),this.items=this.items.filter(function(t,r){return r!==e.detail}),this.emitChanges()},e.prototype.addItemHandler=function(e){this.items=this.items.concat([e.detail]),this.emitChanges()},e.prototype.componentWillLoad=function(){this.validateProps(),this.setOriginalStatus(),this.emitStatusChange()},e.prototype.hostData=function(){return{class:t.getClassNames(this.status,this.isValid(),this.required)}},e.prototype.render=function(){return r(t.Fragment,null,r("dot-label",{label:this.label,required:this.required,name:this.name},r("key-value-form",{"add-button-label":this.formAddButtonLabel,disabled:this.disabled||null,"key-label":this.formKeyLabel,"key-placeholder":this.formKeyPlaceholder,"value-label":this.formValueLabel,"value-placeholder":this.formValuePlaceholder}),r("key-value-table",{onClick:function(e){e.preventDefault()},"button-label":this.listDeleteLabel,disabled:this.disabled||null,items:this.items})),t.getTagHint(this.hint),t.getTagError(this.showErrorMessage(),this.getErrorMessage()))},e.prototype.validateProps=function(){this.valueWatch()},e.prototype.setOriginalStatus=function(){this.status=t.getOriginalStatus(this.isValid())},e.prototype.isValid=function(){return!(this.required&&!this.items.length)},e.prototype.showErrorMessage=function(){return this.getErrorMessage()&&!this.status.dotPristine},e.prototype.getErrorMessage=function(){return this.isValid()?"":this.requiredMessage},e.prototype.refreshStatus=function(){this.status=t.updateStatus(this.status,{dotTouched:!0,dotPristine:!1,dotValid:this.isValid()})},e.prototype.emitStatusChange=function(){this.statusChange.emit({name:this.name,status:this.status})},e.prototype.emitValueChange=function(){var e=t.getStringFromDotKeyArray(this.items);this.valueChange.emit({name:this.name,value:e,fieldType:this.fieldType})},e.prototype.emitChanges=function(){this.refreshStatus(),this.emitStatusChange(),this.emitValueChange()},Object.defineProperty(e,"is",{get:function(){return"dot-key-value"},enumerable:!0,configurable:!0}),Object.defineProperty(e,"properties",{get:function(){return{disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},fieldType:{type:String,attr:"field-type",reflectToAttr:!0},formAddButtonLabel:{type:String,attr:"form-add-button-label",reflectToAttr:!0},formKeyLabel:{type:String,attr:"form-key-label",reflectToAttr:!0},formKeyPlaceholder:{type:String,attr:"form-key-placeholder",reflectToAttr:!0},formValueLabel:{type:String,attr:"form-value-label",reflectToAttr:!0},formValuePlaceholder:{type:String,attr:"form-value-placeholder",reflectToAttr:!0},hint:{type:String,attr:"hint",reflectToAttr:!0},items:{state:!0},label:{type:String,attr:"label",reflectToAttr:!0},listDeleteLabel:{type:String,attr:"list-delete-label",reflectToAttr:!0},name:{type:String,attr:"name",reflectToAttr:!0},required:{type:Boolean,attr:"required",reflectToAttr:!0},requiredMessage:{type:String,attr:"required-message",reflectToAttr:!0},reset:{method:!0},status:{state:!0},value:{type:String,attr:"value",mutable:!0,watchCallbacks:["valueWatch"]}}},enumerable:!0,configurable:!0}),Object.defineProperty(e,"events",{get:function(){return[{name:"valueChange",method:"valueChange",bubbles:!0,cancelable:!0,composed:!0},{name:"statusChange",method:"statusChange",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(e,"listeners",{get:function(){return[{name:"delete",method:"deleteItemHandler"},{name:"add",method:"addItemHandler"}]},enumerable:!0,configurable:!0}),Object.defineProperty(e,"style",{get:function(){return""},enumerable:!0,configurable:!0}),e}(),l={key:"",value:""},n=function(){function e(){this.disabled=!1,this.addButtonLabel="Add",this.keyPlaceholder="",this.valuePlaceholder="",this.keyLabel="Key",this.valueLabel="Value",this.inputs=Object.assign({},l)}return e.prototype.render=function(){var e=this,t=this.isButtonDisabled();return r("form",{onSubmit:this.addKey.bind(this)},r("label",null,this.keyLabel,r("input",{disabled:this.disabled,name:"key",onInput:function(t){return e.setValue(t)},placeholder:this.keyPlaceholder,type:"text",value:this.inputs.key})),r("label",null,this.valueLabel,r("input",{disabled:this.disabled,name:"value",onInput:function(t){return e.setValue(t)},placeholder:this.valuePlaceholder,type:"text",value:this.inputs.value})),r("button",{class:"key-value-form__save__button",type:"submit",disabled:t},this.addButtonLabel))},e.prototype.isButtonDisabled=function(){return!this.isFormValid()||this.disabled||null},e.prototype.isFormValid=function(){return!(!this.inputs.key.length||!this.inputs.value.length)},e.prototype.setValue=function(e){var t;e.stopImmediatePropagation();var r=e.target;this.inputs=Object.assign({},this.inputs,((t={})[r.name]=r.value.toString(),t))},e.prototype.addKey=function(e){e.preventDefault(),e.stopImmediatePropagation(),this.inputs.key&&this.inputs.value&&(this.add.emit(this.inputs),this.clearForm(),this.focusKeyInputField())},e.prototype.clearForm=function(){this.inputs=Object.assign({},l)},e.prototype.focusKeyInputField=function(){this.el.querySelector('input[name="key"]').focus()},Object.defineProperty(e,"is",{get:function(){return"key-value-form"},enumerable:!0,configurable:!0}),Object.defineProperty(e,"properties",{get:function(){return{addButtonLabel:{type:String,attr:"add-button-label",reflectToAttr:!0},disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},inputs:{state:!0},keyLabel:{type:String,attr:"key-label",reflectToAttr:!0},keyPlaceholder:{type:String,attr:"key-placeholder",reflectToAttr:!0},valueLabel:{type:String,attr:"value-label",reflectToAttr:!0},valuePlaceholder:{type:String,attr:"value-placeholder",reflectToAttr:!0}}},enumerable:!0,configurable:!0}),Object.defineProperty(e,"events",{get:function(){return[{name:"add",method:"add",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(e,"style",{get:function(){return"key-value-form form{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}key-value-form form button{margin:0}key-value-form form input{margin:0 1rem 0 .5rem}key-value-form form label{display:-ms-flexbox;display:flex}key-value-form form label,key-value-form form label input{-ms-flex-positive:1;flex-grow:1}"},enumerable:!0,configurable:!0}),e}(),s=function(){function e(){this.items=[],this.disabled=!1,this.buttonLabel="Delete",this.emptyMessage="No values"}return e.prototype.render=function(){return r("table",null,r("tbody",null,this.renderRows(this.items)))},e.prototype.onDelete=function(e){this.delete.emit(e)},e.prototype.getRow=function(e,t){var i=this;return r("tr",null,r("td",null,r("button",{disabled:this.disabled||null,onClick:function(){return i.onDelete(t)},class:"dot-key-value__delete-button"},this.buttonLabel)),r("td",null,e.key),r("td",null,e.value))},e.prototype.renderRows=function(e){return this.isValidItems(e)?e.map(this.getRow.bind(this)):this.getEmptyRow()},e.prototype.getEmptyRow=function(){return r("tr",null,r("td",null,this.emptyMessage))},e.prototype.isValidItems=function(e){return Array.isArray(e)&&!!e.length},Object.defineProperty(e,"is",{get:function(){return"key-value-table"},enumerable:!0,configurable:!0}),Object.defineProperty(e,"properties",{get:function(){return{buttonLabel:{type:String,attr:"button-label",reflectToAttr:!0},disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},emptyMessage:{type:String,attr:"empty-message",reflectToAttr:!0},items:{type:"Any",attr:"items"}}},enumerable:!0,configurable:!0}),Object.defineProperty(e,"events",{get:function(){return[{name:"delete",method:"delete",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),e}();e.DotKeyValue=a,e.KeyValueForm=n,e.KeyValueTable=s,Object.defineProperty(e,"__esModule",{value:!0})});