var __awaiter=this&&this.__awaiter||function(t,e,n,i){return new(n||(n=Promise))(function(s,r){function o(t){try{l(i.next(t))}catch(t){r(t)}}function a(t){try{l(i.throw(t))}catch(t){r(t)}}function l(t){t.done?s(t.value):new n(function(e){e(t.value)}).then(o,a)}l((i=i.apply(t,e||[])).next())})},__generator=this&&this.__generator||function(t,e){var n,i,s,r,o={label:0,sent:function(){if(1&s[0])throw s[1];return s[1]},trys:[],ops:[]};return r={next:a(0),throw:a(1),return:a(2)},"function"==typeof Symbol&&(r[Symbol.iterator]=function(){return this}),r;function a(r){return function(a){return function(r){if(n)throw new TypeError("Generator is already executing.");for(;o;)try{if(n=1,i&&(s=2&r[0]?i.return:r[0]?i.throw||((s=i.return)&&s.call(i),0):i.next)&&!(s=s.call(i,r[1])).done)return s;switch(i=0,s&&(r=[2&r[0],s.value]),r[0]){case 0:case 1:s=r;break;case 4:return o.label++,{value:r[1],done:!1};case 5:o.label++,i=r[1],r=[0];continue;case 7:r=o.ops.pop(),o.trys.pop();continue;default:if(!(s=(s=o.trys).length>0&&s[s.length-1])&&(6===r[0]||2===r[0])){o=0;continue}if(3===r[0]&&(!s||r[1]>s[0]&&r[1]<s[3])){o.label=r[1];break}if(6===r[0]&&o.label<s[1]){o.label=s[1],s=r;break}if(s&&o.label<s[2]){o.label=s[2],o.ops.push(r);break}s[2]&&o.ops.pop(),o.trys.pop();continue}r=e.call(t,o)}catch(t){r=[6,t],i=0}finally{n=s=0}if(5&r[0])throw r[1];return{value:r[0]?r[1]:void 0,done:!0}}([r,a])}}};dotcmsFields.loadBundle("akcehuoc",["exports","./chunk-1c662b11.js","./chunk-3873b584.js"],function(t,e,n){var i=window.dotcmsFields.h,s=n.createCommonjsModule(function(t,e){t.exports=function(){function t(t,e){for(var n=0;n<e.length;n++){var i=e[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(t,i.key,i)}}var e={resultsList:"autoComplete_results_list",result:"autoComplete_result",highlight:"autoComplete_highlighted"},n=function(t){return"string"==typeof t?document.querySelector(t):t()},i=function(t){return t.innerHTML=""},s={getInput:n,createResultsList:function(t){var n=document.createElement("ul");return t.container&&(e.resultsList=t.container(n)||e.resultsList),n.setAttribute("id",e.resultsList),t.destination.insertAdjacentElement(t.position,n),n},highlight:function(t){return"<span class=".concat(e.highlight,">").concat(t,"</span>")},addResultsToList:function(t,n,i,s){n.forEach(function(i,r){var o=document.createElement("li");o.setAttribute("data-result",n[r].value[i.key]||n[r].value),o.setAttribute("class",e.result),o.setAttribute("tabindex","1"),o.innerHTML=s?s(i,o):i.match||i,t.appendChild(o)})},navigation:function(t,e){var i=n(t),s=e.firstChild;document.onkeydown=function(t){var n=document.activeElement;switch(t.keyCode){case 38:n!==s&&n!==i?n.previousSibling.focus():n===s&&i.focus();break;case 40:n===i&&e.childNodes.length>0?s.focus():n!==e.lastChild&&n.nextSibling.focus()}}},clearResults:i,getSelection:function(t,s,r,o){var a=s.querySelectorAll(".".concat(e.result));Object.keys(a).forEach(function(l){["mousedown","keydown"].forEach(function(u){a[l].addEventListener(u,function(a){"mousedown"!==u&&13!==a.keyCode&&39!==a.keyCode||(r({event:a,query:n(t).value,matches:o.matches,results:o.list.map(function(t){return t.value}),selection:o.list.find(function(t){return(t.value[t.key]||t.value)===a.target.closest(".".concat(e.result)).getAttribute("data-result")})}),i(s))})})})}};return function(){function e(t){!function(t,n){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}(this),this.selector=t.selector||"#autoComplete",this.data={src:function(){return"function"==typeof t.data.src?t.data.src():t.data.src},key:t.data.key},this.searchEngine="loose"===t.searchEngine?"loose":"strict",this.threshold=t.threshold||0,this.resultsList=s.createResultsList({container:!(!t.resultsList||!t.resultsList.container)&&t.resultsList.container,destination:t.resultsList&&t.resultsList.destination?t.resultsList.destination:s.getInput(this.selector),position:t.resultsList&&t.resultsList.position?t.resultsList.position:"afterend"}),this.sort=t.sort||!1,this.placeHolder=t.placeHolder,this.maxResults=t.maxResults||5,this.resultItem=t.resultItem,this.highlight=t.highlight||!1,this.onSelection=t.onSelection,this.init()}return n=e,(i=[{key:"search",value:function(t,e){var n=this.highlight,i=e.toLowerCase();if("loose"===this.searchEngine){t=t.replace(/ /g,"");for(var r=[],o=0,a=0;a<i.length;a++){var l=e[a];o<t.length&&i[a]===t[o]&&(l=n?s.highlight(l):l,o++),r.push(l)}return o===t.length&&r.join("")}if(i.includes(t))return t=new RegExp("".concat(t),"i").exec(e),n?e.replace(t,s.highlight(t)):e}},{key:"listMatchedResults",value:function(t){var e=this,n=[],i=s.getInput(this.selector).value.toLowerCase();t.filter(function(t,s){var r=function(r){var o=e.search(i,t[r]||t);o&&r?n.push({key:r,index:s,match:o,value:t}):o&&!r&&n.push({index:s,match:o,value:t})};if(e.data.key){var o=!0,a=!1,l=void 0;try{for(var u,c=e.data.key[Symbol.iterator]();!(o=(u=c.next()).done);o=!0){r(u.value)}}catch(t){a=!0,l=t}finally{try{o||null==c.return||c.return()}finally{if(a)throw l}}}else r()});var r=this.sort?n.sort(this.sort).slice(0,this.maxResults):n.slice(0,this.maxResults);return s.addResultsToList(this.resultsList,r,this.data.key,this.resultItem),s.navigation(this.selector,this.resultsList),{matches:n.length,list:r}}},{key:"ignite",value:function(t){var e=this,n=this.selector,i=s.getInput(n),r=this.placeHolder,o=this.onSelection;r&&i.setAttribute("placeholder",r),i.onkeyup=function(r){var a=e.resultsList;if(s.clearResults(a),i.value.length>e.threshold&&i.value.replace(/ /g,"").length){var l=e.listMatchedResults(t);i.dispatchEvent(new CustomEvent("type",{bubbles:!0,detail:{event:r,query:i.value,matches:l.matches,results:l.list},cancelable:!0})),o&&s.getSelection(n,a,o,l)}}}},{key:"init",value:function(){var t=this,e=this.data.src();e instanceof Promise?e.then(function(e){return t.ignite(e)}):this.ignite(e)}}])&&t(n.prototype,i),e;var n,i}()}()}),r=function(){function t(){this.disabled=!1,this.placeholder="",this.threshold=0,this.maxResults=0,this.debounce=300,this.data=null,this.id="autoComplete"+(new Date).getTime(),this.keyEvent={Enter:this.emitSelection.bind(this),Escape:this.clean.bind(this)}}return t.prototype.componentDidLoad=function(){this.data&&this.initAutocomplete()},t.prototype.render=function(){var t=this;return i("input",{id:this.id,placeholder:this.placeholder||null,disabled:this.disabled||null,onBlur:function(e){return t.handleBlur(e)},onKeyDown:function(e){return t.handleKeyDown(e)}})},t.prototype.watchData=function(){this.autocomplete||this.initAutocomplete()},t.prototype.handleKeyDown=function(t){var e=this.getInputElement().value;e&&this.keyEvent[t.key]&&this.keyEvent[t.key](e)},t.prototype.handleBlur=function(t){var e=this;setTimeout(function(){document.activeElement.parentElement!==e.getResultList()&&(e.clean(),e.lostFocus.emit(t))},0)},t.prototype.clean=function(){this.getInputElement().value="",this.cleanOptions()},t.prototype.cleanOptions=function(){this.getResultList().innerHTML=""},t.prototype.emitSelection=function(t){this.clean(),this.selection.emit(t)},t.prototype.getInputElement=function(){return this.el.querySelector("#"+this.id)},t.prototype.initAutocomplete=function(){var t=this;this.autocomplete=new s({data:{src:function(){return __awaiter(t,void 0,void 0,function(){return __generator(this,function(t){return[2,this.getData()]})})}},sort:function(t,e){return t.match<e.match?-1:t.match>e.match?1:0},placeHolder:this.placeholder,selector:"#"+this.id,threshold:this.threshold,searchEngine:"strict",highlight:!0,maxResults:this.maxResults,debounce:this.debounce,resultsList:{container:function(){return t.getResultListId()},destination:this.getInputElement(),position:"afterend"},resultItem:function(t){return""+t.match},onSelection:function(e){return t.emitSelection(e.selection.value)}})},t.prototype.getResultList=function(){return this.el.querySelector("#"+this.getResultListId())},t.prototype.getResultListId=function(){return this.id+"_results_list"},t.prototype.getData=function(){return __awaiter(this,void 0,void 0,function(){var t,e,n;return __generator(this,function(i){switch(i.label){case 0:return(t=this.getInputElement()).setAttribute("placeholder","Loading..."),this.data?[4,this.data()]:[3,2];case 1:return n=i.sent(),[3,3];case 2:n=[],i.label=3;case 3:return e=n,t.setAttribute("placeholder",this.placeholder||""),[2,e]}})})},Object.defineProperty(t,"is",{get:function(){return"dot-autocomplete"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"properties",{get:function(){return{data:{type:"Any",attr:"data",watchCallbacks:["watchData"]},debounce:{type:Number,attr:"debounce"},disabled:{type:Boolean,attr:"disabled"},el:{elementRef:!0},maxResults:{type:Number,attr:"max-results"},placeholder:{type:String,attr:"placeholder"},threshold:{type:Number,attr:"threshold"}}},enumerable:!0,configurable:!0}),Object.defineProperty(t,"events",{get:function(){return[{name:"selection",method:"selection",bubbles:!0,cancelable:!0,composed:!0},{name:"lostFocus",method:"lostFocus",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(t,"style",{get:function(){return""},enumerable:!0,configurable:!0}),t}(),o=function(){function t(){this.label="",this.deleteLabel="delete",this.disabled=!1}return t.prototype.render=function(){var t=this;return i(e.Fragment,null,i("span",null,this.label),i("button",{type:"button",disabled:this.disabled,onClick:function(){return t.removeTag()}},this.deleteLabel))},t.prototype.removeTag=function(){this.remove.emit(this.label)},Object.defineProperty(t,"is",{get:function(){return"dot-chip"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"properties",{get:function(){return{deleteLabel:{type:String,attr:"delete-label"},disabled:{type:Boolean,attr:"disabled"},el:{elementRef:!0},label:{type:String,attr:"label"}}},enumerable:!0,configurable:!0}),Object.defineProperty(t,"events",{get:function(){return[{name:"remove",method:"remove",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(t,"style",{get:function(){return""},enumerable:!0,configurable:!0}),t}(),a=function(){function t(){this.value="",this.name="",this.label="",this.hint="",this.placeholder="",this.required=!1,this.requiredMessage="",this.disabled=!1,this.threshold=0,this.debounce=300,this.status=e.getOriginalStatus()}return t.prototype.reset=function(){this.value="",this.status=e.getOriginalStatus(this.isValid()),this.emitStatusChange(),this.emitValueChange()},t.prototype.componentWillLoad=function(){this.emitStatusChange()},t.prototype.hostData=function(){return{class:e.getClassNames(this.status,this.isValid(),this.required)}},t.prototype.render=function(){var t=this;return i(e.Fragment,null,i("dot-label",{label:this.label,required:this.required,name:this.name},i("dot-autocomplete",{class:e.getErrorClass(this.status.dotValid),data:this.getData.bind(this),debounce:this.debounce,disabled:this.disabled||null,onLostFocus:function(){return t.blurHandler()},onSelection:function(e){return t.addTag(e.detail)},placeholder:this.placeholder||null,threshold:this.threshold})),i("div",{class:"dot-tags__container"},this.getValues().map(function(e){return i("dot-chip",{disabled:t.disabled,label:e,onRemove:t.removeTag.bind(t)})})),e.getTagHint(this.hint,this.name),e.getTagError(this.showErrorMessage(),this.getErrorMessage()))},t.prototype.setValue=function(){this.status=e.updateStatus(this.status,{dotTouched:!0,dotPristine:!1,dotValid:this.isValid()}),this.emitValueChange(),this.emitStatusChange()},t.prototype.addTag=function(t){var e=this.getValues();e.includes(t)||(e.push(t),this.value=e.join(","),this.selected.emit(t))},t.prototype.removeTag=function(t){var e=this.getValues().filter(function(e){return e!==t.detail});this.value=e.join(","),this.removed.emit(t.detail)},t.prototype.getValues=function(){return this.value?this.value.split(","):[]},t.prototype.isValid=function(){return!this.required||this.required&&!!this.value},t.prototype.showErrorMessage=function(){return this.getErrorMessage()&&!this.status.dotPristine},t.prototype.getErrorMessage=function(){return this.isValid()?"":this.requiredMessage},t.prototype.blurHandler=function(){this.status.dotTouched||(this.status=e.updateStatus(this.status,{dotTouched:!0}),this.emitStatusChange())},t.prototype.emitStatusChange=function(){this.statusChange.emit({name:this.name,status:this.status})},t.prototype.emitValueChange=function(){this.valueChange.emit({name:this.name,value:this.value})},t.prototype.getData=function(){return __awaiter(this,void 0,void 0,function(){return __generator(this,function(t){switch(t.label){case 0:return[4,fetch("https://tarekraafat.github.io/autoComplete.js/demo/db/generic.json")];case 1:return[4,t.sent().json()];case 2:return[2,t.sent().map(function(t){return t.food})]}})})},Object.defineProperty(t,"is",{get:function(){return"dot-tags"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"properties",{get:function(){return{debounce:{type:Number,attr:"debounce"},disabled:{type:Boolean,attr:"disabled"},el:{elementRef:!0},hint:{type:String,attr:"hint"},label:{type:String,attr:"label"},name:{type:String,attr:"name"},placeholder:{type:String,attr:"placeholder"},required:{type:Boolean,attr:"required"},requiredMessage:{type:String,attr:"required-message"},reset:{method:!0},status:{state:!0},threshold:{type:Number,attr:"threshold"},value:{type:String,attr:"value",mutable:!0,watchCallbacks:["setValue"]}}},enumerable:!0,configurable:!0}),Object.defineProperty(t,"events",{get:function(){return[{name:"valueChange",method:"valueChange",bubbles:!0,cancelable:!0,composed:!0},{name:"statusChange",method:"statusChange",bubbles:!0,cancelable:!0,composed:!0},{name:"selected",method:"selected",bubbles:!0,cancelable:!0,composed:!0},{name:"removed",method:"removed",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(t,"style",{get:function(){return"button{border:0}"},enumerable:!0,configurable:!0}),t}();t.DotAutocomplete=r,t.DotChip=o,t.DotTags=a,Object.defineProperty(t,"__esModule",{value:!0})});