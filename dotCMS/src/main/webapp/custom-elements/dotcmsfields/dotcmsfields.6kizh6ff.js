/*! Built with http://stenciljs.com */
((w,d,x,n,h,c,r)=>{((s)=>{s&&(r=s.getAttribute('data-resources-url'))})(d.querySelector("script[data-namespace='dotcmsfields']"));
function e(e,t){return"sc-"+e.t+(t&&t!==l?"-"+t:"")}function t(e,t){return e+(t?"-h":"-s")}function o(e,t){let n,o,l=null,i=!1,s=!1,r=arguments.length;for(;r-- >2;)k.push(arguments[r]);for(;k.length>0;){let t=k.pop();if(t&&void 0!==t.pop)for(r=t.length;r--;)k.push(t[r]);else"boolean"==typeof t&&(t=null),(s="function"!=typeof e)&&(null==t?t="":"number"==typeof t?t=String(t):"string"!=typeof t&&(s=!1)),s&&i?l[l.length-1].vtext+=t:null===l?l=[s?{vtext:t}:t]:l.push(s?{vtext:t}:t),i=s}if(null!=t){if(t.className&&(t.class=t.className),"object"==typeof t.class){for(r in t.class)t.class[r]&&k.push(r);t.class=k.join(" "),k.length=0}null!=t.key&&(n=t.key),null!=t.name&&(o=t.name)}return"function"==typeof e?e(t,l||[],j):{vtag:e,vchildren:l,vtext:void 0,vattrs:t,vkey:n,vname:o,o:void 0,l:!1}}const l="$",i={},s={enter:13,escape:27,space:32,tab:9,left:37,up:38,right:39,down:40},a=(t,n,o,i)=>{let s=o.t+l,r=o[s];if((2===o.i||1===o.i&&!t.u.s)&&(i["s-sc"]=r?e(o,i.mode):e(o)),r){let e=n.p.head,o=t.m.get(e);if(o||t.m.set(e,o={}),!o[s]){let t;{t=r.content.cloneNode(!0),o[s]=!0;const l=e.querySelectorAll("[data-styles]");n.v(e,t,l.length&&l[l.length-1].nextSibling||e.firstChild)}}}},f=e=>null!=e,u=e=>e.toLowerCase(),p=()=>{},b=(e,t,n,o,l,i)=>{if("class"!==n||i)if("style"===n){for(const e in o)l&&null!=l[e]||(/-/.test(e)?t.style.removeProperty(e):t.style[e]="");for(const e in l)o&&l[e]===o[e]||(/-/.test(e)?t.style.setProperty(e,l[e]):t.style[e]=l[e])}else if("o"!==n[0]||"n"!==n[1]||!/[A-Z]/.test(n[2])||n in t)if("list"!==n&&"type"!==n&&!i&&(n in t||-1!==["object","function"].indexOf(typeof l)&&null!==l)){const o=e.M(t);o&&o.g&&o.g[n]?v(t,n,l):"ref"!==n&&(v(t,n,null==l?"":l),null!=l&&!1!==l||e.u.k(t,n))}else null!=l&&"key"!==n?((e,t,n,o="boolean"==typeof n,l)=>{l=t!==(t=t.replace(/^xlink\:?/,"")),null==n||o&&(!n||"false"===n)?l?e.removeAttributeNS("http://www.w3.org/1999/xlink",u(t)):e.removeAttribute(t):"function"!=typeof n&&(n=o?"":n.toString(),l?e.setAttributeNS("http://www.w3.org/1999/xlink",u(t),n):e.setAttribute(t,n))})(t,n,l):(i||e.u.j(t,n)&&(null==l||!1===l))&&e.u.k(t,n);else n=u(n)in t?u(n.substring(2)):u(n[2])+n.substring(3),l?l!==o&&e.u.C(t,n,l):e.u.W(t,n);else if(o!==l){const e=m(o),n=m(l),i=e.filter(e=>!n.includes(e)),s=m(t.className).filter(e=>!i.includes(e)),r=n.filter(t=>!e.includes(t)&&!s.includes(t));s.push(...r),t.className=s.join(" ")}},m=e=>null==e||""===e?[]:e.trim().split(/\s+/),v=(e,t,n)=>{try{e[t]=n}catch(e){}},y=(e,t,n,o,l)=>{const s=11===n.o.nodeType&&n.o.host?n.o.host:n.o,r=t&&t.vattrs||i,a=n.vattrs||i;for(l in r)a&&null!=a[l]||null==r[l]||b(e,s,l,r[l],void 0,o,n.l);for(l in a)l in r&&a[l]===("value"===l||"checked"===l?s[l]:r[l])||b(e,s,l,r[l],a[l],o,n.l)};let M=!1;const g=(e,t)=>{e&&(e.vattrs&&e.vattrs.ref&&e.vattrs.ref(t?null:e.o),e.vchildren&&e.vchildren.forEach(e=>{g(e,t)}))},$=(e,t)=>{{let n=0,o=!1;const l=()=>t.performance.now(),i=!1!==e.asyncQueue,s=Promise.resolve(),r=[],a=[],c=[],f=[],u=t=>n=>{t.push(n),o||(o=!0,e.raf(b))},p=e=>{for(let t=0;t<e.length;t++)try{e[t](l())}catch(e){console.error(e)}e.length=0},d=(e,t)=>{let n,o=0;for(;o<e.length&&(n=l())<t;)try{e[o++](n)}catch(e){console.error(e)}o===e.length?e.length=0:0!==o&&e.splice(0,o)},b=()=>{n++,p(a);const t=i?l()+7*Math.ceil(n*(1/22)):Infinity;d(c,t),d(f,t),c.length>0&&(f.push(...c),c.length=0),(o=a.length+c.length+f.length>0)?e.raf(b):n=0};return e.raf||(e.raf=t.requestAnimationFrame.bind(t)),{tick(e){r.push(e),1===r.length&&s.then(()=>p(r))},read:u(a),write:u(c)}}},k=[],j={forEach:(e,t)=>e.forEach(t),map:(e,t)=>e.map(t)},C=(e,t,n)=>{const[o,l,,i,s,r]=e,a={color:{N:"color"}};if(i)for(t=0;t<i.length;t++)a[(n=i[t])[0]]={O:n[1],S:!!n[2],N:"string"==typeof n[3]?n[3]:n[3]?n[0]:0,A:n[4]};return{t:o,T:l,g:Object.assign({},a),i:s,R:r?r.map(W):void 0}},W=e=>({L:e[0],D:e[1],q:!!e[2],B:!!e[3],I:!!e[4]}),N=(e,t)=>f(t)&&"object"!=typeof t&&"function"!=typeof t?e===Boolean||4===e?"false"!==t&&(""===t||!!t):e===Number||8===e?parseFloat(t):e===String||2===e?t.toString():t:t,O=(e,t,n)=>{e.P.add(t),e.F.has(t)||(e.F.set(t,!0),e.H?e.queue.write(()=>E(e,t,n)):e.queue.tick(()=>E(e,t,n)))},E=async(e,n,l,i,s,r)=>{if(e.F.delete(n),!e.U.has(n)){if(s=e.Z.get(n)){if(s)try{s.componentWillUpdate&&await s.componentWillUpdate()}catch(t){e.G(t,5,n)}}else{if((r=e.J.get(n))&&!r["s-rn"])return void(r["s-rc"]=r["s-rc"]||[]).push(()=>{E(e,n,l)});if(s=D(e,n,e.K.get(n),l))try{s.componentWillLoad&&await s.componentWillLoad()}catch(t){e.G(t,3,n)}}((e,n,l,i)=>{try{const s=n.V.host,r=n.V.encapsulation,a=!1;let c,f=l;if(!l["s-rn"]){e.X(e,e.u,n,l);const o=l["s-sc"];o&&(e.u.Y(l,t(o,!0)),"scoped"===r&&e.u.Y(l,t(o)))}if(i.render||i.hostData||s||c){e._=!0;const t=i.render&&i.render();let n;n=i.hostData&&i.hostData(),e._=!1;const s=o(null,n,t),c=e.ee.get(l)||{};c.o=f,e.ee.set(l,e.render(l,c,s,a,r))}l["s-rn"]=!0,l["s-rc"]&&(l["s-rc"].forEach(e=>e()),l["s-rc"]=null)}catch(t){e._=!1,e.G(t,8,l,!0)}})(e,e.M(n),n,s),n["s-init"]()}},S=(e,t,n,o,l,i,s,r,a)=>{if(t.type||t.state){const c=e.te.get(n);t.state||(!t.attr||void 0!==c[l]&&""!==c[l]||(r=i&&i.ne)&&f(a=r[t.attr])&&(c[l]=N(t.type,a)),n.hasOwnProperty(l)&&(void 0===c[l]&&(c[l]=N(t.type,n[l])),"mode"!==l&&delete n[l])),o.hasOwnProperty(l)&&void 0===c[l]&&(c[l]=o[l]),t.watchCallbacks&&(c[L+l]=t.watchCallbacks.slice()),R(o,l,function c(t){return(t=e.te.get(e.oe.get(this)))&&t[l]},function u(n,o){(o=e.oe.get(this))&&(t.state||t.mutable)&&A(e,o,l,n,s)})}else t.elementRef?T(o,l,n):t.method&&T(n,l,o[l].bind(o))},A=(e,t,n,o,l,i,s)=>{(s=e.te.get(t))||e.te.set(t,s={});const r=s[n];if(o!==r&&(s[n]=o,i=e.Z.get(t))){{const e=s[L+n];if(e)for(let t=0;t<e.length;t++)try{i[e[t]].call(i,o,r,n)}catch(e){console.error(e)}}!e._&&t["s-rn"]&&O(e,t,l)}},T=(e,t,n)=>{Object.defineProperty(e,t,{configurable:!0,value:n})},R=(e,t,n,o)=>{Object.defineProperty(e,t,{configurable:!0,get:n,set:o})},L="wc-",D=(e,t,n,o,l,i,s,r)=>{try{l=new(i=e.M(t).V),((e,t,n,o,l,i)=>{e.oe.set(o,n),e.te.has(n)||e.te.set(n,{}),Object.entries(Object.assign({color:{type:String}},t.properties,{mode:{type:String}})).forEach(([t,s])=>{S(e,s,n,o,t,l,i)})})(e,i,t,l,n,o),function a(e,t,n){if(t){const o=e.oe.get(n);t.forEach(t=>{n[t.method]={emit:n=>e.le(o,t.name,{bubbles:t.bubbles,composed:t.composed,cancelable:t.cancelable,detail:n})}})}}(e,i.events,l);try{if(s=e.ie.get(t)){for(r=0;r<s.length;r+=2)l[s[r]](s[r+1]);e.ie.delete(t)}}catch(n){e.G(n,2,t)}}catch(n){l={},e.G(n,7,t,!0)}return e.Z.set(t,l),l},q=(e,t,n,o,l,i)=>{if(e.P.delete(t),(l=e.J.get(t))&&((o=l["s-ld"])&&((n=o.indexOf(t))>-1&&o.splice(n,1),o.length||l["s-init"]&&l["s-init"]()),e.J.delete(t)),e.se.length&&!e.P.size)for(;i=e.se.shift();)i()},B=(e,t,n,o)=>{t.forEach(([t,l])=>{const i=l.O;3&i?R(n,t,function n(){return(e.te.get(this)||{})[t]},function n(i){A(e,this,t,N(l.A,i),o)}):32===i&&T(n,t,p)})},I=(e,t,n,o,l)=>{if(n.connectedCallback=function(){((e,t,n)=>{e.re.has(n)||(e.re.set(n,!0),function o(e,t){const n=e.M(t);n.R&&n.R.forEach(n=>{n.q||e.u.C(t,n.L,function o(e,t,n,l){return o=>{(l=e.Z.get(t))?l[n](o):((l=e.ie.get(t)||[]).push(n,o),e.ie.set(t,l))}}(e,t,n.D),n.I,n.B)})}(e,n)),e.U.delete(n),e.ae.has(n)||(e.ce=!0,e.P.add(n),e.ae.set(n,!0),((e,t,n)=>{for(n=t;n=e.u.fe(n);)if(e.ue(n)){e.pe.has(t)||(e.J.set(t,n),(n["s-ld"]=n["s-ld"]||[]).push(t));break}})(e,n),e.queue.tick(()=>{e.K.set(n,((e,t,n,o,l)=>(n.mode||(n.mode=e.de(n)),n["s-cr"]||e.be(n,"ssrv")||e.s&&1===t.i||(n["s-cr"]=e.me(""),n["s-cr"]["s-cn"]=!0,e.v(n,n["s-cr"],e.ve(n)[0])),o={ne:{}},t.g&&Object.keys(t.g).forEach(i=>{(l=t.g[i].N)&&(o.ne[l]=e.be(n,l))}),o))(e.u,t,n)),e.ye(t,n)}))})(e,t,this)},n.disconnectedCallback=function(){((e,t)=>{!e.he&&((e,t)=>{for(;t;){if(!e.we(t))return 9!==e.Me(t);t=e.we(t)}})(e.u,t)&&(e.U.set(t,!0),q(e,t),g(e.ee.get(t),!0),e.u.W(t),e.re.delete(t),[e.J,e.ge,e.K].forEach(e=>e.delete(t)))})(e,this)},n["s-init"]=function(){((e,t,n,o,l,i,s)=>{if((l=e.Z.get(t))&&!e.U.has(t)&&(!t["s-ld"]||!t["s-ld"].length)){e.pe.set(t,!0),(s=e.$e.has(t))||(e.$e.set(t,!0),t["s-ld"]=void 0,e.u.Y(t,n));try{g(e.ee.get(t)),(i=e.ge.get(t))&&(i.forEach(e=>e(t)),e.ge.delete(t)),!s&&l.componentDidLoad&&l.componentDidLoad()}catch(n){e.G(n,4,t)}q(e,t)}})(e,this,o)},n.forceUpdate=function(){O(e,this,l)},t.g){const o=Object.entries(t.g);{let e={};o.forEach(([t,{N:n}])=>{n&&(e[n]=t)}),e=Object.assign({},e),n.attributeChangedCallback=function(t,n,o){(function l(e,t,n,o){const l=e[u(n)];l&&(t[l]=o)})(e,this,t,o)}}B(e,o,n,l)}};((e,t,n,i,r,c,p)=>{const d=n.performance,b={html:{}},m=n[e]=n[e]||{},v=((e,t,n)=>{const o=new WeakMap,l={p:n,s:!!n.documentElement.attachShadow,ke:!1,Me:e=>e.nodeType,je:e=>n.createElement(e),Ce:(e,t)=>n.createElementNS(e,t),me:e=>n.createTextNode(e),We:e=>n.createComment(e),v:(e,t,n)=>e.insertBefore(t,n),Ne:e=>e.remove(),Oe:(e,t)=>e.appendChild(t),Y:(e,t)=>{e.classList.add(t)},ve:e=>e.childNodes,we:e=>e.parentNode,xe:e=>e.nextSibling,Ee:e=>e.previousSibling,Se:e=>u(e.nodeName),Ae:e=>e.textContent,Te:(e,t)=>e.textContent=t,be:(e,t)=>e.getAttribute(t),Re:(e,t,n)=>e.setAttribute(t,n),k:(e,t)=>e.removeAttribute(t),j:(e,t)=>e.hasAttribute(t),de:t=>t.getAttribute("mode")||(e.Context||{}).mode,Le:(e,o)=>"child"===o?e.firstElementChild:"parent"===o?l.fe(e):"body"===o?n.body:"document"===o?n:"window"===o?t:e,C:(t,n,i,r,a,c,f,u,p)=>{let d=t,b=i,m=o.get(t);p=n,m&&m[p]&&m[p](),"string"==typeof c?d=l.Le(t,c):"object"==typeof c?d=c:(u=n.split(":")).length>1&&(d=l.Le(t,u[0]),n=u[1]),d&&((u=n.split(".")).length>1&&(n=u[0],b=(e=>{e.keyCode===s[u[1]]&&i(e)})),f=l.ke?{capture:!!r,passive:!!a}:!!r,e.ael(d,n,b,f),m||o.set(t,m={}),m[p]=(()=>{d&&e.rel(d,n,b,f),m[p]=null}))},W:(e,t,n)=>{(n=o.get(e))&&(t?n[t]&&n[t]():Object.keys(n).forEach(e=>{n[e]&&n[e]()}))},De:(e,n,o,l)=>(l=new t.CustomEvent(n,o),e&&e.dispatchEvent(l),l),fe:(e,t)=>(t=l.we(e))&&11===l.Me(t)?t.host:t,qe:(e,t,n,o)=>e.setAttributeNS(t,n,o)};e.ael||(e.ael=((e,t,n,o)=>e.addEventListener(t,n,o)),e.rel=((e,t,n,o)=>e.removeEventListener(t,n,o)));try{t.addEventListener("e",null,Object.defineProperty({},"passive",{get:()=>l.ke=!0}))}catch(e){}return l})(m,n,i),h=v.p.documentElement,w=n["s-defined"]=n["s-defined"]||{},g=(e,t)=>{n.customElements.get(e.t)||(I(k,b[e.t]=e,t.prototype,c,d),t.observedAttributes=Object.values(e.g).map(e=>e.N).filter(e=>!!e),n.customElements.define(e.t,t))},k={u:v,Be:g,M:e=>b[v.Se(e)],Ie:e=>t[e],isClient:!0,ue:e=>!(!w[v.Se(e)]&&!k.M(e)),G:(e,t,n)=>console.error(e,t,n&&n.tagName),queue:t.queue=$(m,n),ye:(e,t)=>{{const n=e.T;let o=r+n+".entry.js";import(o).then(n=>{try{e.V=n[(e=>u(e).split("-").map(e=>e.charAt(0).toUpperCase()+e.slice(1)).join(""))(e.t)],function o(e,t,n,i,s){if(i){const n=t.t+(s||l);if(!t[n]){const o=e.je("template");t[n]=o,o.innerHTML=`<style>${i}</style>`,e.Oe(e.p.head,o)}}}(v,e,e.i,e.V.style,e.V.styleMode),O(k,t,d)}catch(t){console.error(t),e.V=class{}}},e=>console.error(e,o))}},_:!1,H:!1,he:!1,X:a,J:new WeakMap,m:new WeakMap,ae:new WeakMap,re:new WeakMap,$e:new WeakMap,pe:new WeakMap,oe:new WeakMap,K:new WeakMap,Z:new WeakMap,U:new WeakMap,F:new WeakMap,ge:new WeakMap,ie:new WeakMap,ee:new WeakMap,te:new WeakMap,P:new Set,se:[]};return t.isServer=t.isPrerender=!(t.isClient=!0),t.window=n,t.location=n.location,t.document=i,t.resourcesUrl=t.publicPath=r,t.enableListener=((e,t,n,o,l)=>(function i(e,t,n,o,l,s){if(t){const i=e.oe.get(t),r=e.M(i);if(r&&r.R)if(o){const o=r.R.find(e=>e.L===n);o&&e.u.C(i,n,e=>t[o.D](e),o.I,void 0===s?o.B:!!s,l)}else e.u.W(i,n)}})(k,e,t,n,o,l)),k.le=t.emit=((e,n,o)=>v.De(e,t.eventNameFn?t.eventNameFn(n):n,o)),m.h=o,m.Context=t,m.onReady=(()=>new Promise(e=>k.queue.write(()=>k.P.size?k.se.push(e):e()))),k.render=((e,t)=>{let n,o,l,i,s,r,a;const c=(l,p,d,b,m,v,h,w,g)=>{if(w=p.vchildren[d],n||(i=!0,"slot"===w.vtag&&(o&&t.Y(b,o+"-s"),w.vchildren?w.Pe=!0:w.Fe=!0)),f(w.vtext))w.o=t.me(w.vtext);else if(w.Fe)w.o=t.me("");else{if(v=w.o=M||"svg"===w.vtag?t.Ce("http://www.w3.org/2000/svg",w.vtag):t.je(w.Pe?"slot-fb":w.vtag),e.ue(v)&&e.pe.delete(a),M="svg"===w.vtag||"foreignObject"!==w.vtag&&M,y(e,null,w,M),f(o)&&v["s-si"]!==o&&t.Y(v,v["s-si"]=o),w.vchildren)for(m=0;m<w.vchildren.length;++m)(h=c(l,w,m,v))&&t.Oe(v,h);"svg"===w.vtag&&(M=!1)}return w.o["s-hn"]=r,(w.Pe||w.Fe)&&(w.o["s-sr"]=!0,w.o["s-cr"]=s,w.o["s-sn"]=w.vname||"",(g=l&&l.vchildren&&l.vchildren[d])&&g.vtag===w.vtag&&l.o&&u(l.o)),w.o},u=(n,o,l,s)=>{e.he=!0;const a=t.ve(n);for(l=a.length-1;l>=0;l--)(s=a[l])["s-hn"]!==r&&s["s-ol"]&&(t.Ne(s),t.v(v(s),s,m(s)),t.Ne(s["s-ol"]),s["s-ol"]=null,i=!0),o&&u(s,o);e.he=!1},p=(e,n,o,l,i,s,a,u)=>{const p=e["s-cr"];for((a=p&&t.we(p)||e).shadowRoot&&t.Se(a)===r&&(a=a.shadowRoot);i<=s;++i)l[i]&&(u=f(l[i].vtext)?t.me(l[i].vtext):c(null,o,i,e))&&(l[i].o=u,t.v(a,u,m(n)))},d=(e,n,o,i)=>{for(;n<=o;++n)f(e[n])&&(i=e[n].o,l=!0,i["s-ol"]?t.Ne(i["s-ol"]):u(i,!0),t.Ne(i))},b=(e,t)=>e.vtag===t.vtag&&e.vkey===t.vkey&&("slot"!==e.vtag||e.vname===t.vname),m=e=>e&&e["s-ol"]?e["s-ol"]:e,v=e=>t.we(e["s-ol"]?e["s-ol"]:e),h=(n,o,l)=>{const i=o.o=n.o,s=n.vchildren,r=o.vchildren;M=o.o&&f(t.fe(o.o))&&void 0!==o.o.ownerSVGElement,M="svg"===o.vtag||"foreignObject"!==o.vtag&&M,f(o.vtext)?(l=i["s-cr"])?t.Te(t.we(l),o.vtext):n.vtext!==o.vtext&&t.Te(i,o.vtext):("slot"!==o.vtag&&y(e,n,o,M),f(s)&&f(r)?((e,n,o,l,i,s,r,a)=>{let y=0,w=0,M=n.length-1,g=n[0],$=n[M],k=l.length-1,j=l[0],C=l[k];for(;y<=M&&w<=k;)if(null==g)g=n[++y];else if(null==$)$=n[--M];else if(null==j)j=l[++w];else if(null==C)C=l[--k];else if(b(g,j))h(g,j),g=n[++y],j=l[++w];else if(b($,C))h($,C),$=n[--M],C=l[--k];else if(b(g,C))"slot"!==g.vtag&&"slot"!==C.vtag||u(t.we(g.o)),h(g,C),t.v(e,g.o,t.xe($.o)),g=n[++y],C=l[--k];else if(b($,j))"slot"!==g.vtag&&"slot"!==C.vtag||u(t.we($.o)),h($,j),t.v(e,$.o,g.o),$=n[--M],j=l[++w];else{for(i=null,s=y;s<=M;++s)if(n[s]&&f(n[s].vkey)&&n[s].vkey===j.vkey){i=s;break}f(i)?((a=n[i]).vtag!==j.vtag?r=c(n&&n[w],o,i,e):(h(a,j),n[i]=void 0,r=a.o),j=l[++w]):(r=c(n&&n[w],o,w,e),j=l[++w]),r&&t.v(v(g.o),r,m(g.o))}y>M?p(e,null==l[k+1]?null:l[k+1].o,o,l,w,k):w>k&&d(n,y,M)})(i,s,o,r):f(r)?(f(n.vtext)&&t.Te(i,""),p(i,null,o,r,0,r.length-1)):f(s)&&d(s,0,s.length-1)),M&&"svg"===o.vtag&&(M=!1)},w=(e,n,o,l,i,s,r,a)=>{for(l=0,i=(o=t.ve(e)).length;l<i;l++)if(n=o[l],1===t.Me(n)){if(n["s-sr"])for(r=n["s-sn"],n.hidden=!1,s=0;s<i;s++)if(o[s]["s-hn"]!==n["s-hn"])if(a=t.Me(o[s]),""!==r){if(1===a&&r===t.be(o[s],"slot")){n.hidden=!0;break}}else if(1===a||3===a&&""!==t.Ae(o[s]).trim()){n.hidden=!0;break}w(n)}},g=[],$=(e,n,o,i,s,r,a,c,f,u)=>{for(s=0,r=(n=t.ve(e)).length;s<r;s++){if((o=n[s])["s-sr"]&&(i=o["s-cr"]))for(c=t.ve(t.we(i)),f=o["s-sn"],a=c.length-1;a>=0;a--)(i=c[a])["s-cn"]||i["s-nr"]||i["s-hn"]===o["s-hn"]||((3===(u=t.Me(i))||8===u)&&""===f||1===u&&null===t.be(i,"slot")&&""===f||1===u&&t.be(i,"slot")===f)&&(g.some(e=>e.He===i)||(l=!0,i["s-sn"]=f,g.push({Qe:o,He:i})));1===t.Me(o)&&$(o)}};return(c,f,u,p,d,b,m,v,y,M,k,j)=>{if(a=c,r=t.Se(a),s=a["s-cr"],n=p,o=a["s-sc"],i=l=!1,h(f,u),i){for($(u.o),m=0;m<g.length;m++)(v=g[m]).He["s-ol"]||((y=t.me(""))["s-nr"]=v.He,t.v(t.we(v.He),v.He["s-ol"]=y,v.He));for(e.he=!0,m=0;m<g.length;m++){for(v=g[m],k=t.we(v.Qe),j=t.xe(v.Qe),y=v.He["s-ol"];y=t.Ee(y);)if((M=y["s-nr"])&&M&&M["s-sn"]===v.He["s-sn"]&&k===t.we(M)&&(M=t.xe(M))&&M&&!M["s-nr"]){j=M;break}(!j&&k!==t.we(v.He)||t.xe(v.He)!==j)&&v.He!==j&&(t.Ne(v.He),t.v(k,v.He,j))}e.he=!1}return l&&w(u.o),g.length=0,u}})(k,v),h["s-ld"]=[],h["s-rn"]=!0,h["s-init"]=(()=>{k.pe.set(h,m.loaded=k.H=!0),v.De(n,"appload",{detail:{namespace:e}})}),p.map(C).forEach(e=>g(e,class extends HTMLElement{})),k.ce||h["s-init"](),((e,t,n,o,l,i)=>{if(t.componentOnReady=((t,n)=>{if(!t.nodeName.includes("-"))return n(null),!1;const o=e.M(t);if(o)if(e.pe.has(t))n(t);else{const o=e.ge.get(t)||[];o.push(n),e.ge.set(t,o)}return!!o}),l){for(i=l.length-1;i>=0;i--)t.componentOnReady(l[i][0],l[i][1])&&l.splice(i,1);for(i=0;i<o.length;i++)if(!n[o[i]].componentOnReady)return;for(i=0;i<l.length;i++)l[i][1](null);l.length=0}})(k,m,n,n["s-apps"],n["s-cr"]),m.initialized=!0,k})(n,x,w,d,r,h,c);
})(window,document,{},"dotcmsFields","hydrated",[["dot-autocomplete","akcehuoc",1,[["data",1],["debounce",1,0,1,8],["disabled",1,0,1,4],["el",64],["maxResults",1,0,"max-results",8],["placeholder",1,0,1,2],["threshold",1,0,1,8]]],["dot-checkbox","hvthoely",1,[["_options",16],["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["name",1,0,1,2],["options",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["value",2,0,1,2]]],["dot-chip","akcehuoc",1,[["deleteLabel",1,0,"delete-label",2],["disabled",1,0,1,4],["el",64],["label",1,0,1,2]]],["dot-date","hvthoely",1,[["classNames",16],["disabled",1,0,1,4],["el",64],["errorMessageElement",16],["hint",1,0,1,2],["label",1,0,1,2],["max",2,0,1,2],["min",2,0,1,2],["name",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["step",1,0,1,2],["validationMessage",1,0,"validation-message",2],["value",2,0,1,2]],0,[["_valueChange","emitValueChange"],["_statusChange","emitStatusChange"],["_errorMessage","showErrorElement"]]],["dot-date-range","hvthoely",1,[["dateFormat",1,0,"date-format",2],["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["max",1,0,1,2],["min",1,0,1,2],["name",1,0,1,2],["presetLabel",1,0,"preset-label",2],["presets",1],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["value",2,0,1,2]]],["dot-date-time","hvthoely",1,[["classNames",16],["dateLabel",1,0,"date-label",2],["disabled",1,0,1,4],["el",64],["errorMessageElement",16],["hint",1,0,1,2],["label",1,0,1,2],["max",2,0,1,2],["min",2,0,1,2],["name",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["step",2,0,1,2],["timeLabel",1,0,"time-label",2],["validationMessage",1,0,"validation-message",2],["value",2,0,1,2]],0,[["_valueChange","emitValueChange"],["_statusChange","emitStatusChange"],["_errorMessage","showErrorElement"]]],["dot-form","hvthoely",1,[["el",64],["fields",2],["fieldsToShow",1],["resetLabel",1,0,"reset-label",2],["status",16],["submitLabel",1,0,"submit-label",2],["value",2]],0,[["valueChange","onValueChange"],["statusChange","onStatusChange"]]],["dot-input-calendar","vv1fstd3",1,[["disabled",1,0,1,4],["el",64],["max",1,0,1,2],["min",1,0,1,2],["name",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["step",1,0,1,2],["type",1,0,1,2],["validationMessage",1,0,"validation-message",2],["value",2,0,1,2]]],["dot-key-value","y5eglwug",1,[["buttonDeleteLabel",1,0,"button-delete-label",2],["disabled",1,0,1,4],["el",64],["fieldKeyLabel",1,0,"field-key-label",2],["fieldType",1,0,"field-type",2],["fieldValueLabel",1,0,"field-value-label",2],["hint",1,0,1,2],["items",16],["keyPlaceholder",1,0,"key-placeholder",2],["label",1,0,1,2],["name",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["saveBtnLabel",1,0,"save-btn-label",2],["status",16],["value",2,0,1,2],["valuePlaceholder",1,0,"value-placeholder",2]],0,[["deleteItemEvt","deleteItemHandler"]]],["dot-label","vv1fstd3",1,[["label",1,0,1,2],["name",1,0,1,2],["required",1,0,1,4]]],["dot-multi-select","hvthoely",1,[["_options",16],["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["name",1,0,1,2],["options",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["size",1,0,1,8],["status",16],["value",2,0,1,2]]],["dot-radio","hvthoely",1,[["_options",16],["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["name",1,0,1,2],["options",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["value",2,0,1,2]]],["dot-select","hvthoely",1,[["_options",16],["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["name",1,0,1,2],["options",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["value",2,0,1,2]]],["dot-tags","akcehuoc",1,[["debounce",1,0,1,8],["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["name",1,0,1,2],["placeholder",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["threshold",1,0,1,8],["value",2,0,1,2]]],["dot-textarea","hvthoely",1,[["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["name",1,0,1,2],["regexCheck",2,0,"regex-check",2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["validationMessage",1,0,"validation-message",2],["value",2,0,1,2]]],["dot-textfield","hvthoely",1,[["disabled",1,0,1,4],["el",64],["hint",1,0,1,2],["label",1,0,1,2],["name",1,0,1,2],["placeholder",1,0,1,2],["regexCheck",2,0,"regex-check",2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["status",16],["type",1,0,1,2],["validationMessage",1,0,"validation-message",2],["value",2,0,1,2]]],["dot-time","hvthoely",1,[["classNames",16],["disabled",1,0,1,4],["el",64],["errorMessageElement",16],["hint",1,0,1,2],["label",1,0,1,2],["max",2,0,1,2],["min",2,0,1,2],["name",1,0,1,2],["required",1,0,1,4],["requiredMessage",1,0,"required-message",2],["reset",32],["step",1,0,1,2],["validationMessage",1,0,"validation-message",2],["value",2,0,1,2]],0,[["_valueChange","emitValueChange"],["_statusChange","emitStatusChange"],["_errorMessage","setErrorElement"]]],["key-value-table","y5eglwug",0,[["buttonDeleteLabel",1,0,"button-delete-label",2],["disabled",1,0,1,4],["items",1]]]]);