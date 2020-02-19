import{r as t,c as s,h as i,H as h,g as e}from"./p-d624fdf1.js";import{g as a,c as o,k as r,h as n,b as l,d,f as c,e as u,a as m,u as p}from"./p-8f599252.js";import{g,s as b}from"./p-de95ac32.js";const f=class{constructor(i){t(this,i),this.value="",this.name="",this.label="",this.hint="",this.options="",this.required=!1,this.requiredMessage="This field is required",this.disabled=!1,this._dotTouched=!1,this._dotPristine=!0,this.dotValueChange=s(this,"dotValueChange",7),this.dotStatusChange=s(this,"dotStatusChange",7)}componentWillLoad(){this.validateProps(),this.emitInitialValue(),this.status=a(this.isValid()),this.emitStatusChange()}optionsWatch(){const t=o(this,"options");this._options=r(t)}reset(){this.value="",this.status=a(this.isValid()),this.emitInitialValue(),this.emitStatusChange()}componentDidLoad(){const t=this.el.querySelector("select");setTimeout(()=>{const s=g(Array.from(this.el.attributes),[]);b(t,s)},0)}render(){const t=n(this.status,this.isValid(),this.required);return i(h,{class:Object.assign({},t)},i("dot-label",{label:this.label,required:this.required,name:this.name},i("select",{"aria-describedby":c(this.hint),class:u(this.status.dotValid),id:m(this.name),disabled:this.shouldBeDisabled(),onChange:t=>this.setValue(t)},this._options.map(t=>i("option",{selected:this.value===t.value||null,value:t.value},t.label)))),d(this.hint),l(!this.isValid(),this.requiredMessage))}validateProps(){this.optionsWatch()}shouldBeDisabled(){return!!this.disabled||null}setValue(t){this.value=t.target.value,this.status=p(this.status,{dotTouched:!0,dotPristine:!1,dotValid:this.isValid()}),this.emitValueChange(),this.emitStatusChange()}emitInitialValue(){this.value||(this.value=this._options.length?this._options[0].value:"",this.emitValueChange())}emitStatusChange(){this.dotStatusChange.emit({name:this.name,status:this.status})}isValid(){return!this.required||!!this.value}emitValueChange(){this.dotValueChange.emit({name:this.name,value:this.value})}get el(){return e(this)}static get watchers(){return{options:["optionsWatch"]}}static get style(){return""}};export{f as dot_select};