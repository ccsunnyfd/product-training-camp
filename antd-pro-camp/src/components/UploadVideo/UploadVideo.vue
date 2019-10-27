<template>
  <span>
    <a-input
      type="text"
      v-model="title"
      @change="handleTitleChange"
      style="width: 63%; margin-right: 2%;"
    />
    <a-upload
      v-model="videoTrail"
      :action="uploadAction"
      :multiple="true"
      :fileList="fileList"
      @change="handleVideoChange"
      style="width: 30%"
    >
      <a-button> <a-icon type="upload" /> Upload </a-button>
    </a-upload>
  </span>
</template>
<script>
export default {
  name: 'UploadVideo',
  props: {
    maxNum: {
      type: Number,
      default: 1
    },
    uploadAction: {
      type: String,
      required: true
    },
    value: {
      type: Object,
      required: true
    }
  },
  watch: {
    value (val) {
      Object.assign(this, val)
    }
  },
  data () {
    const { title, videoTrail } = this.value || {}
    return {
      title: title || '',
      videoTrail: videoTrail || '',
      fileList: [
        {
          uid: '-1',
          name: title || 'xxx.mp4',
          status: 'done',
          url: videoTrail || 'http://www.baidu.com/xxx.mp4'
        }
      ]
    }
  },
  methods: {
    handleTitleChange (e) {
      const newVal = e.target.value
      this.$emit('change', { ...this.value, title: newVal })
    },
    handleVideoChange (info) {
      let fileList = [...info.fileList]

      // 1. Limit the number of uploaded files
      //    Only to show two recent uploaded files, and old ones will be replaced by the new
      fileList = fileList.slice(-this.maxNum)

      // 2. read from response and show file link
      fileList = fileList.map(file => {
        if (file.response) {
          // Component will show file.url as link
          file.url = file.response.url
          this.$emit('change', { ...this.value, videoTrail: file.url })
        }
        return file
      })
      this.fileList = fileList
    }
  }
}
</script>
