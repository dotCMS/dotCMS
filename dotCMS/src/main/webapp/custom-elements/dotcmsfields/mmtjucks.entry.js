const t=window.dotcmsFields.h;import{a as e}from"./chunk-1d89c98b.js";import{a as s,b as i,d as a,e as l,f as r,h as n,i as o,j as h,k as u}from"./chunk-098a701f.js";import{a as c,b as d}from"./chunk-0e32e502.js";var m=c(function(t,e){t.exports=function(){function t(t,e){for(var s=0;s<e.length;s++){var i=e[s];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(t,i.key,i)}}var e={resultsList:"autoComplete_results_list",result:"autoComplete_result",highlight:"autoComplete_highlighted"},s=function(t){return"string"==typeof t?document.querySelector(t):t()},i=function(t){return t.innerHTML=""},a={getInput:s,createResultsList:function(t){var s=document.createElement("ul");return t.container&&(e.resultsList=t.container(s)||e.resultsList),s.setAttribute("id",e.resultsList),t.destination.insertAdjacentElement(t.position,s),s},highlight:function(t){return"<span class=".concat(e.highlight,">").concat(t,"</span>")},addResultsToList:function(t,s,i,a){s.forEach(function(i,l){var r=document.createElement("li");r.setAttribute("data-result",s[l].value[i.key]||s[l].value),r.setAttribute("class",e.result),r.setAttribute("tabindex","1"),r.innerHTML=a?a(i,r):i.match||i,t.appendChild(r)})},navigation:function(t,e){var i=s(t),a=e.firstChild;document.onkeydown=function(t){var s=document.activeElement;switch(t.keyCode){case 38:s!==a&&s!==i?s.previousSibling.focus():s===a&&i.focus();break;case 40:s===i&&e.childNodes.length>0?a.focus():s!==e.lastChild&&s.nextSibling.focus()}}},clearResults:i,getSelection:function(t,a,l,r){var n=a.querySelectorAll(".".concat(e.result));Object.keys(n).forEach(function(o){["mousedown","keydown"].forEach(function(h){n[o].addEventListener(h,function(n){"mousedown"!==h&&13!==n.keyCode&&39!==n.keyCode||(l({event:n,query:s(t).value,matches:r.matches,results:r.list.map(function(t){return t.value}),selection:r.list.find(function(t){return(t.value[t.key]||t.value)===n.target.closest(".".concat(e.result)).getAttribute("data-result")})}),i(a))})})})}};return function(){function e(t){!function(t,s){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}(this),this.selector=t.selector||"#autoComplete",this.data={src:function(){return"function"==typeof t.data.src?t.data.src():t.data.src},key:t.data.key},this.searchEngine="loose"===t.searchEngine?"loose":"strict",this.threshold=t.threshold||0,this.resultsList=a.createResultsList({container:!(!t.resultsList||!t.resultsList.container)&&t.resultsList.container,destination:t.resultsList&&t.resultsList.destination?t.resultsList.destination:a.getInput(this.selector),position:t.resultsList&&t.resultsList.position?t.resultsList.position:"afterend"}),this.sort=t.sort||!1,this.placeHolder=t.placeHolder,this.maxResults=t.maxResults||5,this.resultItem=t.resultItem,this.highlight=t.highlight||!1,this.onSelection=t.onSelection,this.init()}return s=e,(i=[{key:"search",value:function(t,e){var s=this.highlight,i=e.toLowerCase();if("loose"===this.searchEngine){t=t.replace(/ /g,"");for(var l=[],r=0,n=0;n<i.length;n++){var o=e[n];r<t.length&&i[n]===t[r]&&(o=s?a.highlight(o):o,r++),l.push(o)}return r===t.length&&l.join("")}if(i.includes(t))return t=new RegExp("".concat(t),"i").exec(e),s?e.replace(t,a.highlight(t)):e}},{key:"listMatchedResults",value:function(t){var e=this,s=[],i=a.getInput(this.selector).value.toLowerCase();t.filter(function(t,a){var l=function(l){var r=e.search(i,t[l]||t);r&&l?s.push({key:l,index:a,match:r,value:t}):r&&!l&&s.push({index:a,match:r,value:t})};if(e.data.key){var r=!0,n=!1,o=void 0;try{for(var h,u=e.data.key[Symbol.iterator]();!(r=(h=u.next()).done);r=!0){l(h.value)}}catch(t){n=!0,o=t}finally{try{r||null==u.return||u.return()}finally{if(n)throw o}}}else l()});var l=this.sort?s.sort(this.sort).slice(0,this.maxResults):s.slice(0,this.maxResults);return a.addResultsToList(this.resultsList,l,this.data.key,this.resultItem),a.navigation(this.selector,this.resultsList),{matches:s.length,list:l}}},{key:"ignite",value:function(t){var e=this,s=this.selector,i=a.getInput(s),l=this.placeHolder,r=this.onSelection;l&&i.setAttribute("placeholder",l),i.onkeyup=function(l){var n=e.resultsList;if(a.clearResults(n),i.value.length>e.threshold&&i.value.replace(/ /g,"").length){var o=e.listMatchedResults(t);i.dispatchEvent(new CustomEvent("type",{bubbles:!0,detail:{event:l,query:i.value,matches:o.matches,results:o.list},cancelable:!0})),r&&a.getSelection(s,n,r,o)}}}},{key:"init",value:function(){var t=this,e=this.data.src();e instanceof Promise?e.then(function(e){return t.ignite(e)}):this.ignite(e)}}])&&t(s.prototype,i),e;var s,i}()}()});class p{constructor(){this.disabled=!1,this.placeholder="",this.threshold=0,this.maxResults=0,this.debounce=300,this.data=null,this.id=`autoComplete${(new Date).getTime()}`,this.keyEvent={Enter:this.emitEnter.bind(this),Escape:this.clean.bind(this)}}componentDidLoad(){this.data&&this.initAutocomplete()}render(){return t("input",{id:this.id,placeholder:this.placeholder||null,disabled:this.disabled||null,onBlur:t=>this.handleBlur(t),onKeyDown:t=>this.handleKeyDown(t)})}watchThreshold(){this.initAutocomplete()}watchData(){this.initAutocomplete()}watchMaxResults(){this.initAutocomplete()}handleKeyDown(t){const{value:e}=this.getInputElement();e&&this.keyEvent[t.key]&&(t.preventDefault(),this.keyEvent[t.key](e))}handleBlur(t){t.preventDefault(),setTimeout(()=>{document.activeElement.parentElement!==this.getResultList()&&(this.clean(),this.lostFocus.emit(t))},0)}clean(){this.getInputElement().value="",this.cleanOptions()}cleanOptions(){this.getResultList().innerHTML=""}emitselect(t){this.clean(),this.selection.emit(t)}emitEnter(t){t&&(this.clean(),this.enter.emit(t))}getInputElement(){return this.el.querySelector(`#${this.id}`)}initAutocomplete(){this.clearList(),new m({data:{src:async()=>this.getData()},sort:(t,e)=>t.match<e.match?-1:t.match>e.match?1:0,placeHolder:this.placeholder,selector:`#${this.id}`,threshold:this.threshold,searchEngine:"strict",highlight:!0,maxResults:this.maxResults,debounce:this.debounce,resultsList:{container:()=>this.getResultListId(),destination:this.getInputElement(),position:"afterend"},resultItem:({match:t})=>t,onSelection:({event:t,selection:e})=>{t.preventDefault(),this.focusOnInput(),this.emitselect(e.value)}})}clearList(){const t=this.getResultList();t&&t.remove()}focusOnInput(){this.getInputElement().focus()}getResultList(){return this.el.querySelector(`#${this.getResultListId()}`)}getResultListId(){return`${this.id}_results_list`}async getData(){const t=this.getInputElement();t.setAttribute("placeholder","Loading...");const e="function"==typeof this.data?await this.data():[];return t.setAttribute("placeholder",this.placeholder||""),e}static get is(){return"dot-autocomplete"}static get properties(){return{data:{type:"Any",attr:"data",watchCallbacks:["watchData"]},debounce:{type:Number,attr:"debounce",reflectToAttr:!0},disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},maxResults:{type:Number,attr:"max-results",reflectToAttr:!0,watchCallbacks:["watchMaxResults"]},placeholder:{type:String,attr:"placeholder",reflectToAttr:!0},threshold:{type:Number,attr:"threshold",reflectToAttr:!0,watchCallbacks:["watchThreshold"]}}}static get events(){return[{name:"selection",method:"selection",bubbles:!0,cancelable:!0,composed:!0},{name:"enter",method:"enter",bubbles:!0,cancelable:!0,composed:!0},{name:"lostFocus",method:"lostFocus",bubbles:!0,cancelable:!0,composed:!0}]}static get style(){return"dot-autocomplete input{-webkit-box-sizing:border-box;box-sizing:border-box;width:200px}dot-autocomplete ul{background-color:#fff;list-style:none;margin:0;max-height:300px;overflow:auto;padding:0;position:absolute;width:200px}dot-autocomplete ul li{background-color:#fff;border-top:0;border:1px solid #ccc;-webkit-box-sizing:border-box;box-sizing:border-box;cursor:pointer;padding:.25rem}dot-autocomplete ul li:first-child{border-top:1px solid #ccc}dot-autocomplete ul li:focus{background-color:#ffffe0;outline:0}dot-autocomplete ul li .autoComplete_highlighted{font-weight:700}"}}class g{constructor(){this.label="",this.deleteLabel="Delete",this.disabled=!1}render(){const s=this.label?`${this.deleteLabel} ${this.label}`:null;return t(e,null,t("span",null,this.label),t("button",{type:"button","aria-label":s,disabled:this.disabled,onClick:()=>this.remove.emit(this.label)},this.deleteLabel))}static get is(){return"dot-chip"}static get properties(){return{deleteLabel:{type:String,attr:"delete-label",reflectToAttr:!0},disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},label:{type:String,attr:"label",reflectToAttr:!0}}}static get events(){return[{name:"remove",method:"remove",bubbles:!0,cancelable:!0,composed:!0}]}static get style(){return"dot-chip span{margin-right:.25rem}dot-chip button{cursor:pointer}"}}class b{constructor(){this.value="",this.data=null,this.name="",this.label="",this.hint="",this.placeholder="",this.required=!1,this.requiredMessage="This field is required",this.disabled=!1,this.threshold=0,this.debounce=300}reset(){this.value="",this.status=s(this.isValid()),this.emitChanges()}valueWatch(){this.value=i(this,"value","string")}componentWillLoad(){this.status=s(this.isValid()),this.validateProps(),this.emitStatusChange()}hostData(){return{class:a(this.status,this.isValid(),this.required)}}render(){return t(e,null,t("dot-label",{label:this.label,required:this.required,name:this.name},t("div",{"aria-describedby":l(this.hint),tabIndex:this.hint?0:null,class:"dot-tags__container"},t("dot-autocomplete",{class:r(this.status.dotValid),data:this.data,debounce:this.debounce,disabled:this.isDisabled(),onEnter:this.onEnterHandler.bind(this),onLostFocus:this.blurHandler.bind(this),onSelection:this.onSelectHandler.bind(this),placeholder:this.placeholder||null,threshold:this.threshold}),t("div",{class:"dot-tags__chips"},this.getValues().map(e=>t("dot-chip",{disabled:this.isDisabled(),label:e,onRemove:this.removeTag.bind(this)}))))),n(this.hint),o(this.showErrorMessage(),this.getErrorMessage()))}addTag(t){const e=this.getValues();e.includes(t)||(e.push(t),this.value=e.join(","),this.updateStatus(),this.emitChanges())}blurHandler(){this.status.dotTouched||(this.status=h(this.status,{dotTouched:!0}),this.emitStatusChange())}emitChanges(){this.emitStatusChange(),this.emitValueChange()}emitStatusChange(){this.statusChange.emit({name:this.name,status:this.status})}emitValueChange(){this.valueChange.emit({name:this.name,value:this.value})}getErrorMessage(){return this.isValid()?"":this.requiredMessage}getValues(){return u(this.value)?this.value.split(","):[]}isDisabled(){return this.disabled||null}isValid(){return!this.required||this.required&&!!this.value}onEnterHandler({detail:t=""}){t.split(",").forEach(t=>{this.addTag(t.trim())})}onSelectHandler({detail:t=""}){const e=t.replace(","," ").replace(/\s+/g," ");this.addTag(e)}removeTag(t){const e=this.getValues().filter(e=>e!==t.detail);this.value=e.join(","),this.updateStatus(),this.emitChanges()}showErrorMessage(){return this.getErrorMessage()&&!this.status.dotPristine}updateStatus(){this.status=h(this.status,{dotTouched:!0,dotPristine:!1,dotValid:this.isValid()})}validateProps(){this.valueWatch()}static get is(){return"dot-tags"}static get properties(){return{data:{type:"Any",attr:"data"},debounce:{type:Number,attr:"debounce",reflectToAttr:!0},disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},hint:{type:String,attr:"hint",reflectToAttr:!0},label:{type:String,attr:"label",reflectToAttr:!0},name:{type:String,attr:"name",reflectToAttr:!0},placeholder:{type:String,attr:"placeholder",reflectToAttr:!0},required:{type:Boolean,attr:"required",reflectToAttr:!0},requiredMessage:{type:String,attr:"required-message",reflectToAttr:!0},reset:{method:!0},status:{state:!0},threshold:{type:Number,attr:"threshold",reflectToAttr:!0},value:{type:String,attr:"value",reflectToAttr:!0,mutable:!0,watchCallbacks:["valueWatch"]}}}static get events(){return[{name:"valueChange",method:"valueChange",bubbles:!0,cancelable:!0,composed:!0},{name:"statusChange",method:"statusChange",bubbles:!0,cancelable:!0,composed:!0}]}static get style(){return"dot-tags .dot-tags__container{display:-ms-flexbox;display:flex;-ms-flex-align:start;align-items:flex-start;border:1px solid #d3d3d3}dot-tags .dot-tags__container dot-autocomplete{margin:.5rem 1rem .5rem .5rem}dot-tags .dot-tags__container .dot-tags__chips{margin:.5rem 1rem 0 0}dot-tags .dot-tags__container dot-chip{border:1px solid #ccc;display:inline-block;margin:0 .5rem .5rem 0;padding:.2rem}dot-tags button{border:0}"}}export{p as DotAutocomplete,g as DotChip,b as DotTags};