const t=window.dotcmsFields.h;import{a as e,b as s}from"./chunk-0e32e502.js";import{a as i,b as a,c as l,d as n,e as r,f as o,g as h}from"./chunk-4e11e8c2.js";var u=e(function(t,e){t.exports=function(){function t(t,e){for(var s=0;s<e.length;s++){var i=e[s];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(t,i.key,i)}}var e={resultsList:"autoComplete_results_list",result:"autoComplete_result",highlight:"autoComplete_highlighted"},s=function(t){return"string"==typeof t?document.querySelector(t):t()},i=function(t){return t.innerHTML=""},a={getInput:s,createResultsList:function(t){var s=document.createElement("ul");return t.container&&(e.resultsList=t.container(s)||e.resultsList),s.setAttribute("id",e.resultsList),t.destination.insertAdjacentElement(t.position,s),s},highlight:function(t){return"<span class=".concat(e.highlight,">").concat(t,"</span>")},addResultsToList:function(t,s,i,a){s.forEach(function(i,l){var n=document.createElement("li");n.setAttribute("data-result",s[l].value[i.key]||s[l].value),n.setAttribute("class",e.result),n.setAttribute("tabindex","1"),n.innerHTML=a?a(i,n):i.match||i,t.appendChild(n)})},navigation:function(t,e){var i=s(t),a=e.firstChild;document.onkeydown=function(t){var s=document.activeElement;switch(t.keyCode){case 38:s!==a&&s!==i?s.previousSibling.focus():s===a&&i.focus();break;case 40:s===i&&e.childNodes.length>0?a.focus():s!==e.lastChild&&s.nextSibling.focus()}}},clearResults:i,getSelection:function(t,a,l,n){var r=a.querySelectorAll(".".concat(e.result));Object.keys(r).forEach(function(o){["mousedown","keydown"].forEach(function(h){r[o].addEventListener(h,function(r){"mousedown"!==h&&13!==r.keyCode&&39!==r.keyCode||(l({event:r,query:s(t).value,matches:n.matches,results:n.list.map(function(t){return t.value}),selection:n.list.find(function(t){return(t.value[t.key]||t.value)===r.target.closest(".".concat(e.result)).getAttribute("data-result")})}),i(a))})})})}};return function(){function e(t){!function(t,s){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}(this),this.selector=t.selector||"#autoComplete",this.data={src:function(){return"function"==typeof t.data.src?t.data.src():t.data.src},key:t.data.key},this.searchEngine="loose"===t.searchEngine?"loose":"strict",this.threshold=t.threshold||0,this.resultsList=a.createResultsList({container:!(!t.resultsList||!t.resultsList.container)&&t.resultsList.container,destination:t.resultsList&&t.resultsList.destination?t.resultsList.destination:a.getInput(this.selector),position:t.resultsList&&t.resultsList.position?t.resultsList.position:"afterend"}),this.sort=t.sort||!1,this.placeHolder=t.placeHolder,this.maxResults=t.maxResults||5,this.resultItem=t.resultItem,this.highlight=t.highlight||!1,this.onSelection=t.onSelection,this.init()}return s=e,(i=[{key:"search",value:function(t,e){var s=this.highlight,i=e.toLowerCase();if("loose"===this.searchEngine){t=t.replace(/ /g,"");for(var l=[],n=0,r=0;r<i.length;r++){var o=e[r];n<t.length&&i[r]===t[n]&&(o=s?a.highlight(o):o,n++),l.push(o)}return n===t.length&&l.join("")}if(i.includes(t))return t=new RegExp("".concat(t),"i").exec(e),s?e.replace(t,a.highlight(t)):e}},{key:"listMatchedResults",value:function(t){var e=this,s=[],i=a.getInput(this.selector).value.toLowerCase();t.filter(function(t,a){var l=function(l){var n=e.search(i,t[l]||t);n&&l?s.push({key:l,index:a,match:n,value:t}):n&&!l&&s.push({index:a,match:n,value:t})};if(e.data.key){var n=!0,r=!1,o=void 0;try{for(var h,u=e.data.key[Symbol.iterator]();!(n=(h=u.next()).done);n=!0){l(h.value)}}catch(t){r=!0,o=t}finally{try{n||null==u.return||u.return()}finally{if(r)throw o}}}else l()});var l=this.sort?s.sort(this.sort).slice(0,this.maxResults):s.slice(0,this.maxResults);return a.addResultsToList(this.resultsList,l,this.data.key,this.resultItem),a.navigation(this.selector,this.resultsList),{matches:s.length,list:l}}},{key:"ignite",value:function(t){var e=this,s=this.selector,i=a.getInput(s),l=this.placeHolder,n=this.onSelection;l&&i.setAttribute("placeholder",l),i.onkeyup=function(l){var r=e.resultsList;if(a.clearResults(r),i.value.length>e.threshold&&i.value.replace(/ /g,"").length){var o=e.listMatchedResults(t);i.dispatchEvent(new CustomEvent("type",{bubbles:!0,detail:{event:l,query:i.value,matches:o.matches,results:o.list},cancelable:!0})),n&&a.getSelection(s,r,n,o)}}}},{key:"init",value:function(){var t=this,e=this.data.src();e instanceof Promise?e.then(function(e){return t.ignite(e)}):this.ignite(e)}}])&&t(s.prototype,i),e;var s,i}()}()});class c{constructor(){this.disabled=!1,this.placeholder="",this.threshold=0,this.maxResults=0,this.debounce=300,this.data=null,this.id=`autoComplete${(new Date).getTime()}`,this.keyEvent={Enter:this.emitSelection.bind(this),Escape:this.clean.bind(this)}}componentDidLoad(){this.data&&this.initAutocomplete()}render(){return t("input",{id:this.id,placeholder:this.placeholder||null,disabled:this.disabled||null,onBlur:t=>this.handleBlur(t),onKeyDown:t=>this.handleKeyDown(t)})}watchData(){this.autocomplete||this.initAutocomplete()}handleKeyDown(t){const e=this.getInputElement().value;e&&this.keyEvent[t.key]&&this.keyEvent[t.key](e)}handleBlur(t){setTimeout(()=>{document.activeElement.parentElement!==this.getResultList()&&(this.clean(),this.lostFocus.emit(t))},0)}clean(){this.getInputElement().value="",this.cleanOptions()}cleanOptions(){this.getResultList().innerHTML=""}emitSelection(t){this.clean(),this.selection.emit(t)}getInputElement(){return this.el.querySelector(`#${this.id}`)}initAutocomplete(){this.autocomplete=new u({data:{src:async()=>this.getData()},sort:(t,e)=>t.match<e.match?-1:t.match>e.match?1:0,placeHolder:this.placeholder,selector:`#${this.id}`,threshold:this.threshold,searchEngine:"strict",highlight:!0,maxResults:this.maxResults,debounce:this.debounce,resultsList:{container:()=>this.getResultListId(),destination:this.getInputElement(),position:"afterend"},resultItem:t=>`${t.match}`,onSelection:t=>this.emitSelection(t.selection.value)})}getResultList(){return this.el.querySelector(`#${this.getResultListId()}`)}getResultListId(){return`${this.id}_results_list`}async getData(){const t=this.getInputElement();t.setAttribute("placeholder","Loading...");const e=this.data?await this.data():[];return t.setAttribute("placeholder",this.placeholder||""),e}static get is(){return"dot-autocomplete"}static get properties(){return{data:{type:"Any",attr:"data",watchCallbacks:["watchData"]},debounce:{type:Number,attr:"debounce"},disabled:{type:Boolean,attr:"disabled"},el:{elementRef:!0},maxResults:{type:Number,attr:"max-results"},placeholder:{type:String,attr:"placeholder"},threshold:{type:Number,attr:"threshold"}}}static get events(){return[{name:"selection",method:"selection",bubbles:!0,cancelable:!0,composed:!0},{name:"lostFocus",method:"lostFocus",bubbles:!0,cancelable:!0,composed:!0}]}static get style(){return""}}class d{constructor(){this.label="",this.deleteLabel="delete",this.disabled=!1}render(){return t(i,null,t("span",null,this.label),t("button",{type:"button",disabled:this.disabled,onClick:()=>this.removeTag()},this.deleteLabel))}removeTag(){this.remove.emit(this.label)}static get is(){return"dot-chip"}static get properties(){return{deleteLabel:{type:String,attr:"delete-label"},disabled:{type:Boolean,attr:"disabled"},el:{elementRef:!0},label:{type:String,attr:"label"}}}static get events(){return[{name:"remove",method:"remove",bubbles:!0,cancelable:!0,composed:!0}]}static get style(){return""}}class m{constructor(){this.value="",this.name="",this.label="",this.hint="",this.placeholder="",this.required=!1,this.requiredMessage="",this.disabled=!1,this.threshold=0,this.debounce=300,this.status=a()}reset(){this.value="",this.status=a(this.isValid()),this.emitStatusChange(),this.emitValueChange()}componentWillLoad(){this.emitStatusChange()}hostData(){return{class:l(this.status,this.isValid(),this.required)}}render(){return t(i,null,t("dot-label",{label:this.name,required:this.required,name:this.name},t("dot-autocomplete",{class:n(this.status.dotValid),data:this.getData.bind(this),debounce:this.debounce,disabled:this.disabled||null,onLostFocus:()=>this.blurHandler(),onSelection:t=>this.addTag(t.detail),placeholder:this.placeholder||null,threshold:this.threshold})),t("div",{class:"dot-tags__container"},this.getValues().map(e=>t("dot-chip",{disabled:this.disabled,label:e,onRemove:this.removeTag.bind(this)}))),r(this.hint,this.name),o(this.showErrorMessage(),this.getErrorMessage()))}setValue(){this.status=h(this.status,{dotTouched:!0,dotPristine:!1,dotValid:this.isValid()}),this.emitValueChange(),this.emitStatusChange()}addTag(t){const e=this.getValues();e.includes(t)||(e.push(t),this.value=e.join(","),this.selected.emit(t))}removeTag(t){const e=this.getValues().filter(e=>e!==t.detail);this.value=e.join(","),this.removed.emit(t.detail)}getValues(){return this.value?this.value.split(","):[]}isValid(){return!this.required||this.required&&!!this.value}showErrorMessage(){return this.getErrorMessage()&&!this.status.dotPristine}getErrorMessage(){return this.isValid()?"":this.requiredMessage}blurHandler(){this.status.dotTouched||(this.status=h(this.status,{dotTouched:!0}),this.emitStatusChange())}emitStatusChange(){this.statusChange.emit({name:this.name,status:this.status})}emitValueChange(){this.valueChange.emit({name:this.name,value:this.value})}async getData(){const t=await fetch("https://tarekraafat.github.io/autoComplete.js/demo/db/generic.json");return(await t.json()).map(t=>t.food)}static get is(){return"dot-tags"}static get properties(){return{debounce:{type:Number,attr:"debounce"},disabled:{type:Boolean,attr:"disabled"},el:{elementRef:!0},hint:{type:String,attr:"hint"},label:{type:String,attr:"label"},name:{type:String,attr:"name"},placeholder:{type:String,attr:"placeholder"},required:{type:Boolean,attr:"required"},requiredMessage:{type:String,attr:"required-message"},reset:{method:!0},status:{state:!0},threshold:{type:Number,attr:"threshold"},value:{type:String,attr:"value",mutable:!0,watchCallbacks:["setValue"]}}}static get events(){return[{name:"valueChange",method:"valueChange",bubbles:!0,cancelable:!0,composed:!0},{name:"statusChange",method:"statusChange",bubbles:!0,cancelable:!0,composed:!0},{name:"selected",method:"selected",bubbles:!0,cancelable:!0,composed:!0},{name:"removed",method:"removed",bubbles:!0,cancelable:!0,composed:!0}]}static get style(){return"button{border:0}"}}export{c as DotAutocomplete,d as DotChip,m as DotTags};