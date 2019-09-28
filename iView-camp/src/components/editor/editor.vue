<template>
  <div class="editor-wrapper">
    <div :id="editorId"></div>
  </div>
</template>

<script>
import Editor from 'wangeditor'
import lrz from 'lrz'
import 'wangeditor/release/wangEditor.min.css'
import { oneOf } from '@/libs/tools'
import { uploadImg, removeObject } from '@/api/data.js'
export default {
  name: 'Editor',
  props: {
    value: {
      type: String,
      default: ''
    },
    /**
     * 绑定的值的类型, enum: ['html', 'text']
     */
    valueType: {
      type: String,
      default: 'html',
      validator: (val) => {
        return oneOf(val, ['html', 'text'])
      }
    },
    /**
     * @description 设置change事件触发时间间隔
     */
    changeInterval: {
      type: Number,
      default: 200
    },
    /**
     * @description 是否开启本地存储
     */
    cache: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      // 富文本编辑器已上传过的图片url
      postedImgList: []
    }
  },
  computed: {
    editorId () {
      return `editor${this._uid}`
    },
    editorEle () {
      if (document.querySelector(`#${this.editorId}`)) {
        return document.querySelector(`#${this.editorId}`)
      }
    },
    toolbar () {
      if (document.querySelector(`#${this.editorId} .w-e-toolbar`)) {
        return document.querySelector(`#${this.editorId} .w-e-toolbar`)
      }
    }
  },
  methods: {
    arrayMinus (arr1, arr2) {
      var m = {}
      if (arr1.length > 0) {
        arr1.forEach((al) => {
          m[al] = al
        })
      }
      if (arr2.length > 0) {
        arr2.forEach((bl) => {
          delete m[bl]
        })
      }
      return Object.keys(m)
    },
    removeDeletedImg () {
      const reg = /<img\b.*?(?:>|\/>)/gi // 全局匹配<img >的子串
      const regSrc = /\bsrc\b\s*=\s*['"]?([^'"]*)['"]?/i // 匹配子串中的url部分
      let editorImgUrl = [] // 编辑框内剩余的图片url
      let matchList = localStorage.editorCache.match(reg) // 全局匹配<img >的子串列表
      for (let index in matchList) {
        let srcStr = matchList[index].match(regSrc)
        if (srcStr.length > 0) {
          editorImgUrl.push(srcStr[1])
        }
      }
      let localStorageImgUrl = JSON.parse((localStorage.getItem('postedImgList')))
      // 将记录的上传过的图片url数组和从编辑框获得的图片url数组相减，获得编辑删除的图片url
      let editorDeletedUrl = this.arrayMinus(localStorageImgUrl, editorImgUrl)
      // 过滤掉不是s3图片服务器地址的外域的url
      const s3Url = this.$config.s3Url
      const s3Urlreg = new RegExp(s3Url)
      const serverUrlreg = /:\/\/(.*?)\/(.*?)\/(.*)/
      // 过滤掉并发起图片删除请求
      for (let index in editorDeletedUrl) {
        if (s3Urlreg.test(editorDeletedUrl[index])) {
          const objectName = serverUrlreg.exec(editorDeletedUrl[index])[3]
          removeObject(objectName).then((resp) => {
            //
          }).catch(() => {
            //
          })
        }
      }
    },
    setHtml (val) {
      this.editor.txt.html(val)
    },
    initFullscreen () {
      let enlarge = '<i title="全屏" class="ivu-icon ivu-icon-md-expand"></i>'
      // let enlarge = '<i title="全屏" class="ivu-icon ivu-icon-arrow-expand"></i>'
      let shrink = '<i title="退出全屏" class="ivu-icon ivu-icon-ios-expand"></i>'
      // let shrink = '<i title="退出全屏" class="ivu-icon ivu-icon-arrow-shrink"></i>'
      let isFullscreen = false
      let fullscreenBtn = this.createBtn(enlarge)
      this.toolbar.appendChild(fullscreenBtn)
      fullscreenBtn.addEventListener('click', _ => {
        this.editorEle.className = isFullscreen ? '' : 'fullscreen-editor'
        fullscreenBtn.innerHTML = isFullscreen ? enlarge : shrink
        isFullscreen = !isFullscreen
      }, false)
    },
    createBtn (btnHtml) {
      let btn = document.createElement('div')
      btn.className = 'w-e-menu'
      // btn.style.zIndex = '10001'
      btn.innerHTML = btnHtml
      return btn
    }
  },
  mounted () {
    let that = this
    this.editor = new Editor(`#${this.editorId}`)
    this.editor.customConfig.onchange = (html) => {
      let text = this.editor.txt.text()
      if (this.cache) {
        localStorage.editorCache = html
        // 保存已上传图片列表到localStorage中
        localStorage.setItem('postedImgList', JSON.stringify(that.postedImgList))
      }
      this.$emit('input', this.valueType === 'html' ? html : text)
      this.$emit('on-change', html, text)
    }
    // 自定义菜单配置
    this.editor.customConfig.menus = [
      'head', // 标题
      'bold', // 粗体
      'fontSize', // 字号
      'fontName', // 字体
      'italic', // 斜体
      'underline', // 下划线
      'strikeThrough', // 删除线
      'foreColor', // 文字颜色
      'backColor', // 背景颜色
      'link', // 插入链接
      'list', // 列表
      'justify', // 对齐方式
      'quote', // 引用
      'emoticon', // 表情
      'image', // 插入图片
      'table', // 表格
      'code', // 插入代码
      'undo', // 撤销
      'redo' // 重复
    ]
    this.editor.customConfig.onchangeTimeout = this.changeInterval
    // zIndex 防止工具栏图标droplist被遮挡，鼠标会点不上去
    this.editor.customConfig.zIndex = 101
    // 默认是false，允许粘贴内容中有图片
    this.editor.customConfig.pasteIgnoreImg = false
    this.editor.customConfig.uploadImgServer = '/upload'
    this.editor.customConfig.customUploadImg = function (files, insert) {
      // files是input中选中的文件列表
      // insert是获取图片url后，插入到编辑器的方法
      files.forEach((item) => {
        // lrz用于在前端压缩图片
        lrz(item, { width: 400, height: 200 }).then((rst) => {
          // 处理成功会执行
          item = rst.file
        }).catch(() => {
          // 处理失败会执行
          console.log('图片压缩失败~')
        }).always(() => {
          // 不管是成功失败，都会执行
          const formData = new FormData()
          formData.append('file', item)
          uploadImg(formData).then((resp) => {
            if (resp.data.status === 'success') {
              that.$Message.success('图片上传成功~', 2)
              // 上传代码返回结果之后，将图片插入到编辑器中
              insert(resp.data.url)
              that.postedImgList.push(resp.data.url)
            } else {
              that.$Message.warning('图片上传失败，请联系管理员~', 3)
            }
          }).catch((err) => {
            that.$Message.warning('图片上传异常，请联系管理员~' + err, 600)
          })
        })
      })
    }
    // 将图片大小限制为 4M
    this.editor.customConfig.uploadImgMaxSize = 4 * 1024 * 1024
    // 限制一次最多上传 10 张图片
    this.editor.customConfig.uploadImgMaxLength = 10
    // withCredentials（跨域传递 cookie）
    this.editor.customConfig.withCredentials = true
    // 将 timeout 时间改为 3s
    this.editor.customConfig.uploadImgTimeout = 3000
    // create这个方法一定要在所有配置项之后调用
    this.editor.create()
    // 初始化全屏预览
    this.initFullscreen()
    // 如果本地有存储加载本地存储内容
    let html = this.value || localStorage.editorCache
    if (html) this.editor.txt.html(html)
    // 如果本地有存储已上传图片url列表，加载本地存储内容
    that.postedImgList = that.postedImgList ? that.postedImgList : JSON.parse(localStorage.getItem('postedImgList'))
  }
}
</script>

<style lang="less">
.w-e-toolbar {
    flex-wrap: wrap;
    -webkit-box-lines: multiple;
}

.w-e-toolbar .w-e-menu:hover{
    z-index: 10002!important;
}

.w-e-menu a {
    text-decoration: none;
}

.fullscreen-editor {
    position: fixed !important;
    width: 100% !important;
    height: 100% !important;
    left: 0px !important;
    top: 0px !important;
    background-color: white;
    z-index: 9999;
}

.fullscreen-editor .w-e-text-container {
    width: 100% !important;
    height: 95% !important;
}
</style>
