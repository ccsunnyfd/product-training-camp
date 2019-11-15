<template>
  <div class="clearfix">
    <a-upload
      multiple
      accept=".mp4"
      :beforeUpload="beforeUpload"
      :remove="handleRemove"
      :fileList="fileList"
      class="upload-list-inline"
    >
      <a-button> <a-icon type="upload" /> 选择文件（最多一次选2个） </a-button>
    </a-upload>
    <a-button
      type="primary"
      @click="handleUpload"
      :disabled="fileList.length === 0"
      :loading="uploading"
      style="margin-top: 16px"
    >
      {{ uploading ? 'Uploading' : 'Start Upload' }}
    </a-button>

  </div>
</template>
<script>
import { uploadVideos } from '@/api/data.js'
export default {
  name: 'UploadVideo',
  props: {
    maxNum: {
      type: Number,
      default: 2
    }
  },
  data () {
    return {
      fileList: [],
      uploading: false
    }
  },
  methods: {
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
    },
    beforeUpload (file, fileList) {
      const isMP4 = file.type === 'video/mp4'
      if (!isMP4) {
        const index = fileList.indexOf(file)
        fileList.splice(index, 1)
        this.$message.error('只支持mp4的视频格式，将不会上传' + file.name + '！', 4)
        return false
      }
      const isLt1G = file.size / 1024 / 1024 / 1024 < 1
      if (!isLt1G) {
        const index = fileList.indexOf(file)
        fileList.splice(index, 1)
        this.$message.error(file.name + '图片大小超出1G限制，请修改后重新上传', 4)
        return false
      }
      this.fileList = fileList
      console.log(this.fileList)
      return false
    },
    handleUpload () {
      const { fileList } = this
      const fileNum = fileList.length
      // 1. Limit the number of uploaded files
      if (fileNum > this.maxNum) {
        this.$message.error('一次最多只能上传' + this.maxNum + '个视频，请删除掉一些后重新上传', 4)
        return false
      }

      // 2. read from response and show file link
      const formData = new FormData()
      fileList.forEach(file => {
        formData.append('files', file)
      })
      this.uploading = true
      // 上传
      uploadVideos(formData).then((res) => {
        // const res = response.data
        if (res.status === 'success') {
          this.uploading = false
          this.fileList = []
          this.$message.success('视频上传成功~', 3)
          // 上传代码返回结果之后，将包含name和url的video对象数组作为事件返回值返回
          this.$emit('change', res.videoList)
        } else {
          this.uploading = false
          this.$message.warning('视频上传失败，请联系管理员~', 3)
        }
      }).catch((err) => {
        this.uploading = false
        this.$message.warning('视频上传异常，请联系管理员~' + err, 10)
      })

      this.fileList = []
    }
  }
}
</script>
<style scoped>
  /* tile uploaded pictures */
  .upload-list-inline >>> .ant-upload-list-item {
    float: left;
    width: 200px;
    margin-right: 8px;
  }
  .upload-list-inline >>> .ant-upload-animate-enter {
    animation-name: uploadAnimateInlineIn;
  }
  .upload-list-inline >>> .ant-upload-animate-leave {
    animation-name: uploadAnimateInlineOut;
  }
</style>
