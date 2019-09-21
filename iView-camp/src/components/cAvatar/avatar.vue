<template>
<div>
  <Upload
    ref="upload"
    :show-upload-list="false"
    :on-success="handleSuccess"
    :format="['jpg','jpeg','png']"
    :max-size="2048"
    :before-upload="changeUpload"
    type="drag"
    action=""
    style="display:inline-block; width:110px; height: 100%;">
    <div class="ProfileHeader-main">
      <div class="UserAvatarEditor ProfileHeader-avatar">
        <div class="UserAvatar">
          <img class="Avatar Avatar--large UserAvatar-inner" width="100" height="100" :src="displayImg">
        </div>
        <label class="UploadPicture-wrapper">
          <div class="Mask UserAvatarEditor-mask">
            <div class="Mask-mask Mask-mask--black UserAvatarEditor-maskInner">
            </div>
            <div class="Mask-content">
              <svg class="Zi Zi--Camera UserAvatarEditor-cameraIcon" fill="currentColor" viewBox="0 0 24 24" width="36" height="36">
                <path
                  d="M20.094 6S22 6 22 8v10.017S22 20 19 20H4.036S2 20 2 18V7.967S2 6 4 6h3s1-2 2-2h6c1 0 2 2 2 2h3.094zM12 16a3.5 3.5 0 1 1 0-7 3.5 3.5 0 0 1 0 7zm0 1.5a5 5 0 1 0-.001-10.001A5 5 0 0 0 12 17.5zm7.5-8a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"
                  fill-rule="evenodd">
                </path>
              </svg>
              <div class="UserAvatarEditor-maskInnerText">
                修改图片
              </div>
            </div>
          </div>
        </label>
      </div>
    </div>

      <!-- <Icon type="ios-camera" size="20"></Icon>
      <div class="UserAvatarEditor-maskInnerText">
        修改图片
      </div> -->
  </Upload>
 <!-- vueCropper 剪裁图片实现-->
  <Modal v-model="cropVisible" :closable="false" :mask-closable="false">
    <p slot="header" style="text-align:center">
      <span style="font-size:18px;font-weight:500;color:#1a1a1a;">
        图片剪裁
      </span>
    </p>
    <div style="text-align:center;font-size:14px;line-height:1.5;color:#8590a6;">
      调整图像尺寸和位置
    </div>
    <div class="cropper-content">
      <div class="cropper" style="width: auto; height: 300px; text-align:center">
      <vueCropper
          ref="cropper"
          :img="option.img"
          :outputSize="option.size"
          :outputType="option.outputType"
          :info="true"
          :full="option.full"
          :canMove="option.canMove"
          :canMoveBox="option.canMoveBox"
          :original="option.original"
          :autoCrop="option.autoCrop"
          :fixed="option.fixed"
          :fixedNumber="option.fixedNumber"
          :centerBox="option.centerBox"
          :infoTrue="option.infoTrue"
          :fixedBox="option.fixedBox"
          :autoCropWidth="option.autoCropWidth"
          :autoCropHeight="option.autoCropHeight"
        ></vueCropper>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <Button @click="cropVisible = false">取 消</Button>
      <Button type="primary" @click="finish" :loading="loading">确认</Button>
    </div>
  </Modal>
  </div>
</template>
<script>
import defaultAvatar from '@/assets/images/userAvatar.jpg'
import { VueCropper } from 'vue-cropper'
import { uploadImg, removeObject } from '@/api/data.js'
export default {
  name: 'Cavatar',
  components: {
    VueCropper
  },
  props: {
    fileinfo: {
      type: String,
      default: ''
    },
    cropWidth: {
      type: Number,
      default: 200
    },
    cropHeight: {
      type: Number,
      default: 200
    }
  },
  data () {
    return {
      cropVisible: false,
      // 裁剪组件的基础配置option
      option: {
        img: '', // 裁剪图片的地址
        info: true, // 裁剪框的大小信息
        outputSize: 0.8, // 裁剪生成图片的质量
        outputType: 'jpeg', // 裁剪生成图片的格式
        canScale: true, // 图片是否允许滚轮缩放
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 200, // 默认生成截图框宽度
        autoCropHeight: 200, // 默认生成截图框高度
        fixedBox: true, // 固定截图框大小 不允许改变
        fixed: true, // 是否开启截图框宽高固定比例
        // fixedNumber: [3, 2], // 截图框的宽高比例
        full: true, // 是否输出原图比例的截图
        canMoveBox: false, // 截图框能否拖动
        original: false, // 上传图片按照原始比例渲染
        centerBox: true, // 截图框是否被限制在图片里面
        infoTrue: true // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
      },
      loading: false
    }
  },
  watch: {
    cropWidth () {
      this.option.autoCropWidth = this.cropWidth
    },
    cropHeight () {
      this.option.autoCropHeight = this.cropHeight
    }
  },
  computed: {
    displayImg () {
      return this.fileinfo === '' ? defaultAvatar : this.fileinfo
    },
    uploadList () {
      return this.$refs.upload ? this.$refs.upload.fileList : []
    }
  },
  methods: {
    // 上传按钮   限制图片类型和大小
    changeUpload (file, fileList) {
      const isImgType = ['image/png', 'image/jpeg'].includes(file.type)
      const isLt5M = file.size / 1024 / 1024 < 5
      if (!isImgType) {
        this.handleFormatError()
        return false
      }
      if (!isLt5M) {
        this.handleMaxSize()
        return false
      }

      // 上传成功后将图片地址赋值给裁剪框显示图片
      this.$nextTick(() => {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = (event) => {
          this.option.img = event.srcElement.result
        }
        this.cropVisible = true
      })
      return false
    },
    // 点击裁剪，这一步是可以拿到处理后的地址
    finish () {
      this.$refs.cropper.getCropBlob((blob) => {
        this.loading = true
        if (this.fileinfo !== '') {
          this.handleRemove(this.fileinfo)
        }
        const formData = new FormData()
        formData.append('file', blob)
        uploadImg(formData).then((resp) => {
          if (resp.data.status === 'success') {
            this.loading = false
            this.cropVisible = false
            this.$Message.success('上传成功~', 3)
            this.$emit('imgUpdate', resp.data.url)
          } else {
            this.$Message.warning('上传失败，请联系管理员~', 5)
            this.loading = false
          }
        }).catch(err => {
          this.$Message.warning('上传失败，请联系管理员~' + err, 5)
          this.loading = false
        })
        // 上传到阿里云OSS
        // client().put(fileName, data).then(result => {
        //   this.dialogVisible = false
        //   this.picsList.push(result.url)
        // }).catch(err => {
        //   console.log(err)
        //   this.loading = false
        // })
      })
    },
    // handleView (name) {
    //   this.imgName = name
    //   this.visible = true
    // },
    handleRemove (imgUrl) {
      const reg = /:\/\/(.*?)\/(.*?)\/(.*)/
      const objectName = reg.exec(imgUrl)[3]
      // 从 S3 删除图片
      removeObject(objectName).then((resp) => {
        //
      }).catch(() => {
        //
      })
    },
    handleSuccess (res, file) {
      // 因为上传过程为实例，这里模拟添加 url
      // file.url = 'https://o5wwk8baw.qnssl.com/7eb99afb9d5f317c912f08b5212fd69a/avatar'
      // file.name = '7eb99afb9d5f317c912f08b5212fd69a'
    },
    handleFormatError () {
      this.$Notice.warning({
        title: '文件格式不正确',
        desc: '文件格式不正确，请上传 jpg 或 png 格式的图片。'
      })
    },
    handleMaxSize () {
      this.$Notice.warning({
        title: '超出文件大小限制',
        desc: '上传文件大小不能超过5M。'
      })
    }
    // handleBeforeUpload () {
    //   const check = this.uploadList.length < 5
    //   if (!check) {
    //     this.$Notice.warning({
    //       title: '最多只能上传 5 张图片。'
    //     })
    //   }
    //   return check
    // }
  },
  mounted () {

  }
}
</script>

<style lang="less">
  @import './avatar.less';
</style>
