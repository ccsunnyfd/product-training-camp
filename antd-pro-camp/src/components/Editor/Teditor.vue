<template>
  <div class="editor-wrapper">
    <div
      :id="editorId"
    >
    </div>
  </div>
</template>

<script>
import Editor from 'wangeditor'
import lrz from 'lrz'
import 'wangeditor/release/wangEditor.min.css'
import { oneOf } from '@/utils/tools'
import { s3Url, uploadImg, removeFiles } from '@/api/data.js'
import { filterS3UrlArray, getImgUrlArray } from '@/components/_util/util'
export default {
  name: 'Teditor',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    value: {
      type: String
    },
    /**
     * 绑定的值的类型, enum: ['html', 'text']
     */
    valueType: {
      type: String,
      default: 'html',
      validator: val => {
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
      default: false // 使用form表单项的形式，通过vuex保存状态，暂不需要开启本地存储
    }
  },
  watch: {
    value (val) {
      this.content = val || ''
      this.setHtml(this.content)
    }
  },
  data () {
    const value = this.value
    return {
      content: value || '',
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
      return null
    },
    toolbar () {
      if (document.querySelector(`#${this.editorId} .w-e-toolbar`)) {
        return document.querySelector(`#${this.editorId} .w-e-toolbar`)
      }
      return null
    }
  },
  methods: {
    resetPostedImg () {
      this.postedImgList = []
    },
    arrayMinus (arr1, arr2) {
      var m = {}
      if (arr1.length > 0) {
        arr1.forEach(al => {
          m[al] = al
        })
      }
      if (arr2.length > 0) {
        arr2.forEach(bl => {
          delete m[bl]
        })
      }
      return Object.keys(m)
    },
    removeImgRequest (editorDeletedUrlArray) {
      const urlNeedDelete = filterS3UrlArray(s3Url, editorDeletedUrlArray)
      if (urlNeedDelete.length === 0) {
        return
      }
      removeFiles(urlNeedDelete)
        .then(resp => {
          if (resp.status === 'success') {
            this.$message.success(resp.msg, 2)
          } else if (resp.failedFiles) {
            this.$message.warning(resp.msg, 2)
            console.log(resp.msg)
            console.log(resp.failedFiles)
          } else {
            this.$message.warning(resp.msg, 2)
          }
        })
        .catch(() => {
          this.$message.warning('服务器异常~', 2)
        })
    },
    // removeAllImg () {
    //   this.removeImgRequest(this.postedImgList)
    // },
    removeDeletedImg () {
      const editorImgUrl = getImgUrlArray(this.editor.txt.html())
      // console.log(editorImgUrl)
      // const localStorageImgUrl = JSON.parse((localStorage.getItem('postedImgList')))
      // 将记录的上传过的图片url数组和从编辑框获得的图片url数组相减，获得编辑删除的图片url
      // const editorDeletedUrl = this.arrayMinus(localStorageImgUrl, editorImgUrl)
      const editorDeletedUrlArray = this.arrayMinus(this.postedImgList, editorImgUrl)
      this.removeImgRequest(editorDeletedUrlArray)
    },
    setHtml (val) {
      this.editor.txt.html(val)
    },
    getHtml () {
      return this.editor.txt.html()
    },
    initFullscreen () {
      // const enlarge = '<i title="全屏" class="ivu-icon ivu-icon-md-expand"></i>'
      const enlarge = '<button>全屏</button>'
      // let enlarge = '<i title="全屏" class="ivu-icon ivu-icon-arrow-expand"></i>'
      // const shrink = '<i title="退出全屏" class="ivu-icon ivu-icon-ios-expand"></i>'
      const shrink = '<button>关闭全屏</button>'
      // let shrink = '<i title="退出全屏" class="ivu-icon ivu-icon-arrow-shrink"></i>'
      let isFullscreen = false
      const fullscreenBtn = this.createBtn(enlarge)
      this.toolbar.appendChild(fullscreenBtn)
      fullscreenBtn.addEventListener(
        'click',
        _ => {
          this.editorEle.className = isFullscreen ? '' : 'fullscreen-editor'
          fullscreenBtn.innerHTML = isFullscreen ? enlarge : shrink
          isFullscreen = !isFullscreen
        },
        false
      )
    },
    createBtn (btnHtml) {
      const btn = document.createElement('div')
      btn.className = 'w-e-menu'
      // btn.style.zIndex = '10001'
      btn.innerHTML = btnHtml
      return btn
    }
  },
  mounted () {
    const _this = this
    this.editor = new Editor(`#${this.editorId}`)
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
      'code' // 插入代码
      // 'undo', // 撤销
      // 'redo' // 重复
    ]
    // zIndex 防止工具栏图标droplist被遮挡，鼠标会点不上去
    this.editor.customConfig.zIndex = 101
    // 默认是false，允许粘贴内容中有图片
    this.editor.customConfig.pasteIgnoreImg = false
    this.editor.customConfig.uploadImgServer = '/upload'
    this.editor.customConfig.onchange = (html) => {
      this.$emit('input', html)
    }
    this.editor.customConfig.onchangeTimeout = this.changeInterval
    this.editor.customConfig.customUploadImg = function (files, insert) {
      // files是input中选中的文件列表
      // insert是获取图片url后，插入到编辑器的方法
      files.forEach(item => {
        const filename = item.name
        // lrz用于在前端压缩图片
        lrz(item, { width: 400 })
          .then(rst => {
            // 处理成功会执行
            item = rst.file
          })
          .catch(() => {
            // 处理失败会执行
            console.log('图片压缩失败~')
          })
          .always(() => {
            // 不管是成功失败，都会执行
            const formData = new FormData()
            formData.append('file', item, filename)
            uploadImg(formData)
              .then(res => {
                // const res = response.data
                if (res.status === 'success') {
                  _this.$message.success('图片上传成功~', 2)
                  // 上传代码返回结果之后，将图片插入到编辑器中
                  insert(res.url)
                  _this.postedImgList.push(res.url)
                } else {
                  _this.$message.warning('图片上传失败，请联系管理员~', 3)
                }
              })
              .catch(err => {
                _this.$message.warning('图片上传异常，请联系管理员~' + err, 600)
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
    // const html = this.value || localStorage.editorCache
    // 如果本地有存储已上传图片url列表，加载本地存储内容
    // _this.postedImgList = _this.postedImgList ? _this.postedImgList : JSON.parse(localStorage.getItem('postedImgList'))
    this.postedImgList = []
  }
}
</script>

<style lang="less">
.w-e-toolbar {
  flex-wrap: wrap;
  -webkit-box-lines: multiple;
}

.w-e-toolbar .w-e-menu:hover {
  z-index: 10002 !important;
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
