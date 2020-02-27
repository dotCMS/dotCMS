var __awaiter=this&&this.__awaiter||function(e,t,i,s){function r(e){return e instanceof i?e:new i((function(t){t(e)}))}return new(i||(i=Promise))((function(i,a){function o(e){try{l(s.next(e))}catch(t){a(t)}}function n(e){try{l(s["throw"](e))}catch(t){a(t)}}function l(e){e.done?i(e.value):r(e.value).then(o,n)}l((s=s.apply(e,t||[])).next())}))};var __generator=this&&this.__generator||function(e,t){var i={label:0,sent:function(){if(a[0]&1)throw a[1];return a[1]},trys:[],ops:[]},s,r,a,o;return o={next:n(0),throw:n(1),return:n(2)},typeof Symbol==="function"&&(o[Symbol.iterator]=function(){return this}),o;function n(e){return function(t){return l([e,t])}}function l(o){if(s)throw new TypeError("Generator is already executing.");while(i)try{if(s=1,r&&(a=o[0]&2?r["return"]:o[0]?r["throw"]||((a=r["return"])&&a.call(r),0):r.next)&&!(a=a.call(r,o[1])).done)return a;if(r=0,a)o=[o[0]&2,a.value];switch(o[0]){case 0:case 1:a=o;break;case 4:i.label++;return{value:o[1],done:false};case 5:i.label++;r=o[1];o=[0];continue;case 7:o=i.ops.pop();i.trys.pop();continue;default:if(!(a=i.trys,a=a.length>0&&a[a.length-1])&&(o[0]===6||o[0]===2)){i=0;continue}if(o[0]===3&&(!a||o[1]>a[0]&&o[1]<a[3])){i.label=o[1];break}if(o[0]===6&&i.label<a[1]){i.label=a[1];a=o;break}if(a&&i.label<a[2]){i.label=a[2];i.ops.push(o);break}if(a[2])i.ops.pop();i.trys.pop();continue}o=t.call(e,i)}catch(n){o=[6,n];r=0}finally{s=a=0}if(o[0]&5)throw o[1];return{value:o[0]?o[1]:void 0,done:true}}};System.register(["./p-aa15c74d.system.js","./p-835fa369.system.js","./p-495705cb.system.js","./p-8b2e6c31.system.js"],(function(e){"use strict";var t,i,s,r,a,o,n,l,h,u,d,p,c,f,y;return{setters:[function(e){t=e.r;i=e.c;s=e.h;r=e.H;a=e.g},function(e){o=e.h;n=e.c;l=e.a;h=e.f;u=e.b;d=e.u;p=e.m},function(e){c=e.g;f=e.s},function(e){y=e.D}],execute:function(){var g=e("dot_binary_file",function(){function e(e){t(this,e);this.name="";this.label="";this.placeholder="Drop or paste a file or url";this.hint="";this.required=false;this.requiredMessage="This field is required";this.validationMessage="The field doesn't comply with the specified format";this.URLValidationMessage="The specified URL is not valid";this.disabled=false;this.accept="";this.maxFileLength="";this.buttonLabel="Browse";this.errorMessage="";this.previewImageName="";this.previewImageUrl="";this.file=null;this.allowedFileTypes=[];this.errorMessageMap=new Map;this.dotValueChange=i(this,"dotValueChange",7);this.dotStatusChange=i(this,"dotStatusChange",7)}e.prototype.reset=function(){return __awaiter(this,void 0,void 0,(function(){return __generator(this,(function(e){this.file="";this.binaryTextField.value="";this.errorMessage="";this.clearPreviewData();this.status=o(this.isValid());this.emitStatusChange();this.emitValueChange();return[2]}))}))};e.prototype.clearValue=function(){return __awaiter(this,void 0,void 0,(function(){return __generator(this,(function(e){this.binaryTextField.value="";this.errorType=this.required?y.REQUIRED:null;this.setValue("");this.clearPreviewData();return[2]}))}))};e.prototype.componentWillLoad=function(){this.setErrorMessageMap();this.validateProps();this.status=o(this.isValid());this.emitStatusChange()};e.prototype.componentDidLoad=function(){var e=this;this.binaryTextField=this.el.querySelector("dot-binary-text-field");var t=["dottype"];var i=this.el.querySelector('input[type="file"]');setTimeout((function(){var s=c(Array.from(e.el.attributes),t);f(i,s)}),0)};e.prototype.requiredMessageWatch=function(){this.errorMessageMap.set(y.REQUIRED,this.requiredMessage)};e.prototype.validationMessageWatch=function(){this.errorMessageMap.set(y.INVALID,this.validationMessage)};e.prototype.URLValidationMessageWatch=function(){this.errorMessageMap.set(y.URLINVALID,this.URLValidationMessage)};e.prototype.optionsWatch=function(){this.accept=n(this,"accept");this.allowedFileTypes=!!this.accept?this.accept.split(","):[];this.allowedFileTypes=this.allowedFileTypes.map((function(e){return e.trim()}))};e.prototype.fileChangeHandler=function(e){e.stopImmediatePropagation();var t=e.detail;this.errorType=t.errorType;this.setValue(t.file);if(this.isBinaryUploadButtonEvent(e.target)&&t.file){this.binaryTextField.value=t.file.name}};e.prototype.HandleDragover=function(e){e.preventDefault();if(!this.disabled){this.el.classList.add("dot-dragover");this.el.classList.remove("dot-dropped")}};e.prototype.HandleDragleave=function(e){e.preventDefault();this.el.classList.remove("dot-dragover");this.el.classList.remove("dot-dropped")};e.prototype.HandleDrop=function(e){e.preventDefault();this.el.classList.remove("dot-dragover");if(!this.disabled&&!this.previewImageName){this.el.classList.add("dot-dropped");this.errorType=null;var t=e.dataTransfer.files[0];this.handleDroppedFile(t)}};e.prototype.handleDelete=function(e){e.preventDefault();this.setValue("");this.clearPreviewData()};e.prototype.render=function(){var e=l(this.status,this.isValid(),this.required);return s(r,{class:Object.assign({},e)},s("dot-label",{label:this.label,required:this.required,name:this.name,tabindex:"0"},this.previewImageName?s("dot-binary-file-preview",{onClick:function(e){e.preventDefault()},fileName:this.previewImageName,previewUrl:this.previewImageUrl}):s("div",{class:"dot-binary__container"},s("dot-binary-text-field",{placeholder:this.placeholder,required:this.required,disabled:this.disabled,accept:this.allowedFileTypes.join(","),hint:this.hint,onLostFocus:this.lostFocusEventHandler.bind(this)}),s("dot-binary-upload-button",{name:this.name,accept:this.allowedFileTypes.join(","),disabled:this.disabled,required:this.required,buttonLabel:this.buttonLabel}))),u(this.hint),h(this.shouldShowErrorMessage(),this.getErrorMessage()),s("dot-error-message",null,this.errorMessage))};e.prototype.lostFocusEventHandler=function(){if(!this.status.dotTouched){this.status=d(this.status,{dotTouched:true});this.emitStatusChange()}};e.prototype.isBinaryUploadButtonEvent=function(e){return e.localName==="dot-binary-upload-button"};e.prototype.validateProps=function(){this.optionsWatch();this.setPlaceHolder()};e.prototype.shouldShowErrorMessage=function(){return this.getErrorMessage()&&!this.status.dotPristine};e.prototype.getErrorMessage=function(){return this.errorMessageMap.get(this.errorType)};e.prototype.isValid=function(){return!(this.required&&!this.file)};e.prototype.setErrorMessageMap=function(){this.requiredMessageWatch();this.validationMessageWatch();this.URLValidationMessageWatch()};e.prototype.setValue=function(e){this.file=e;this.status=d(this.status,{dotTouched:true,dotPristine:false,dotValid:this.isValid()});this.binaryTextField.value=e===null?"":this.binaryTextField.value;this.emitValueChange();this.emitStatusChange()};e.prototype.emitStatusChange=function(){this.dotStatusChange.emit({name:this.name,status:this.status})};e.prototype.emitValueChange=function(){this.dotValueChange.emit({name:this.name,value:this.file})};e.prototype.handleDroppedFile=function(e){if(p(e.name,this.allowedFileTypes.join(","))){this.setValue(e);this.binaryTextField.value=e.name}else{this.errorType=y.INVALID;this.setValue(null)}};e.prototype.setPlaceHolder=function(){var e="Drop a file or url";this.placeholder=this.isWindowsOS()?e:this.placeholder};e.prototype.isWindowsOS=function(){return window.navigator.platform.includes("Win")};e.prototype.clearPreviewData=function(){this.previewImageUrl="";this.previewImageName=""};Object.defineProperty(e.prototype,"el",{get:function(){return a(this)},enumerable:true,configurable:true});Object.defineProperty(e,"watchers",{get:function(){return{requiredMessage:["requiredMessageWatch"],validationMessage:["validationMessageWatch"],URLValidationMessage:["URLValidationMessageWatch"],accept:["optionsWatch"]}},enumerable:true,configurable:true});Object.defineProperty(e,"style",{get:function(){return"dot-binary-file.dot-dragover input{background-color:#f1f1f1}dot-binary-file .dot-binary__container button,dot-binary-file .dot-binary__container input{display:-ms-inline-flexbox;display:inline-flex;border:1px solid #d3d3d3;padding:15px;border-radius:0}dot-binary-file .dot-binary__container input[type=file]{display:none}dot-binary-file .dot-binary__container input{min-width:245px;text-overflow:ellipsis}dot-binary-file .dot-binary__container button{background-color:#d3d3d3}"},enumerable:true,configurable:true});return e}())}}}));