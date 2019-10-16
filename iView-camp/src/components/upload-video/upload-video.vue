<template>
    <div>
        <div class="desc">仅支持MP4格式的视频，且大小不要超过1GB。</div>
        <Row>
            <i-col :span="12">
                <Upload
                  ref="upload"
                  :show-upload-list="false"
                  :on-change="handleChange"
                  :on-success="handleSuccess"
                  :format="['mp4']"
                  :max-size="1024000"
                  :on-format-error="handleFormatError"
                  :on-exceeded-size="handleMaxSize"
                  :before-upload="handleBeforeUpload"
                  :action="Upurl"
                  style="display: inline-block;width:195px;float:left;">
                  <div style="width: 195px;height:130px;line-height: 130px;text-align:center;border:1px solid #dddee1;cursor:pointer;">
                    <Icon type="ios-add" size="40" color="#dddee1"></Icon>
                  </div>
                </Upload>
                <div class="demo-upload-list fl" v-for="(item, index) in uploadList" :key="item.url">
                    <template v-if="item.status === 'finished'">
                      <i class="ivu-icon ivu-icon-ios-checkmark-circle" style="color: #19be6b;"></i>
                      <span style="margin-left: 10px;">已上传: {{ item.name }}</span>
                      <div class="demo-upload-list-cover">
                          <Icon type="ios-close" @click.native="handleRemove(item, index)" class="del-btn"></Icon>
                      </div>
                    </template>
                    <template v-else>
                      <Progress :percent="item.percentage" hide-info />
                      <!-- <i-circle v-if="item.showProgress" :percent="item.percentage" stroke-color="#e63b24" :stroke-width="3">
                        <span class="demo-Circle-inner" style="font-size:10px">{{ parseInt(item.percentage) }}%</span>
                      </i-circle> -->
                    </template>
                </div>

            </i-col>
        </Row>
    </div>
</template>
<script>
export default {
  name: 'UploadVideo',
  props: {
    editVideo: {
      required: true,
      type: Array
    }
  },
  data () {
    return {
      uploadList: [],
      // uptoken: '',
      Upurl: (process.env.NODE_ENV === 'development' ? this.$config.baseUrl.dev : this.$config.baseUrl.pro) + 'minioS3/uploadStream'
    }
  },
  methods: {
    // 上传前回调
    handleBeforeUpload (file) {
      const check = this.uploadList.length < 3
      if (!check) {
        this.$Notice.warning({
          title: '不能超过3个视频'
        })
        return false
      }
    },
    handleChange (file, fileList) {
      console.log(file, fileList)
    },
    handleRemove (item, index) {
      const fileList = this.$refs.upload.fileList
      this.$refs.upload.fileList.splice(fileList.indexOf(item), 1)
      this.$emit('on-del-video', index)
    },
    // 上传成功回调
    handleSuccess (res, file) {
      // console.log(res)
      // console.log(file)
      file.url = res.url
      file.status = 'finished'
      // file.width = '195'
      // file.height = '110'
      this.$emit('on-add-video', file)
    },
    handleFormatError (file) {
      this.$Notice.warning({
        title: '文件格式不正确',
        desc: '文件' + file.name + '格式不正确，请上传mp4格式'
      })
    },
    handleMaxSize (file) {
      this.$Notice.warning({
        title: '超出文件大小范围',
        desc: '文件' + file.name + ' 太大了, 不允许超过 1GB.'
      })
    }
    // // 获取token
    // getToken () {
    //   return new Promise((resolve, reject) => {
    //     this.axios.get(this.$config.uploadUrl + 'file/uptoken')
    //       .then(res => {
    //           let data = res.data;
    //           if (data.code === 200) {
    //               this.uptoken = data.data.uptoken;
    //               resolve()
    //           }
    //       }).catch(err => {
    //         reject(err)
    //       })
    //   })
    // }
  },
  mounted () {
    this.$refs.upload.fileList = this.$refs.upload.fileList.concat(this.editVideo)
    this.uploadList = this.$refs.upload.fileList
  }
}
</script>
<style scoped>
  .demo-upload-list{
    display: inline-block;
    width: 100%;
    height: 50px;
    text-align: center;
    line-height: 50px;
    border: 1px solid transparent;
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    position: relative;
    box-shadow: 0 1px 1px rgba(0,0,0,.2);
    margin-right: 4px;
  }
  .demo-upload-list img{
    width: 100%;
    height: 100%;
  }
  .demo-upload-list-cover{
    display: none;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0,0,0,.6);
  }
  .demo-upload-list:hover .demo-upload-list-cover{
    display: block;
  }
  .demo-upload-list-cover i{
    color: #fff;
    font-size: 5px;
    cursor: pointer;
    margin: 0 2px;
  }
  .del-btn{
    position: absolute;
    top: 5px;
    right: 10px;
  }
  .desc{
    color: #999
  }
</style>
