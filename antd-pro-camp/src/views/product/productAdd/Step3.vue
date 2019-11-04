<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <!-- 视频上传编辑器 -->
      <div>
        <upload-video
          :maxNum="3"
          @change="handleVideoChange" />
      </div>
      <draggable
        v-model="step.courseList"
        @update="datadragEnd"
        animation="500">
        <transition-group>
          <div v-for="(element, index) in step.courseList" :key="element.url" class="drag-item">
            <span class="drag-name">{{ index + 1 }}&nbsp&nbsp
              <span
                v-focus="element.editable"
                :contenteditable="element.editable"
                v-text="element.name"
                @blur.stop="handleInput(index, $event)"
              >
              </span>
            </span>
            <span class="drag-button">
              <a-button size="small" type="link" :disabled="element.editable" @click.stop="handleEdit(index, $event)">
                编辑
              </a-button>
              <a-button size="small" type="danger" @click.stop="handleDelete(index)">删除</a-button>
            </span>
            </span>
          </div>
        </transition-group>
      </draggable>
      <span v-show="step.courseList.length != 0" class="drag-hint">
        * 拖动以调整课时视频的先后顺序
      </span>

      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button :disabled="!canSubmit" :loading="loading" type="primary" @click="nextStep">提交</a-button>
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import UploadVideo from '@/components/UploadVideo/UploadVideo'
import draggable from 'vuedraggable'
import { oneOf } from '../../../utils/tools'
import { removeObject } from '@/api/data.js'

export default {
  name: 'Step2',
  components: {
    UploadVideo,
    draggable
  },
  data () {
    this.form = this.$form.createForm(this)
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 19 }, sm: { span: 19 } },
      loading: false,
      timer: 0,
      editable: false
    }
  },
  computed: {
    step () {
      return this.$store.state.form.step
    },
    canSubmit () {
      if (this.step.courseList.length === 0) {
        return false
      }
      const validList = this.step.courseList.map(item => item.editable)
      if (oneOf(true, validList)) {
        return false
      }
      return true
    }
  },
  mounted () {
    // 为了防止火狐浏览器拖拽的时候以新标签打开，此代码真实有效
    document.body.ondrop = function (event) {
      event.preventDefault()
      event.stopPropagation()
    }
  },
  directives: {
    focus: {
      update: function (el, { value }) {
        if (value) {
          el.focus()
        }
      }
    }
  },
  methods: {
    getdata (evt) {
      console.log(evt.draggedContext.element.text)
    },
    handleVideoChange (videoList) {
      const { $store, step } = this
      videoList.forEach(element => {
        element.editable = false
      })
      $store.commit({
        type: 'form/saveStepFormData',
        payload: { ...step, courseList: videoList }
      })
    },
    handleEdit (index, $event) {
      this.step.courseList[index].editable = true
    },
    handleInput (index, $event) {
      const innerText = $event.target.innerText
      if (innerText === '') {
        this.$message.warning('课时标题不能为空', 3)
        this.step.courseList[index].name = '课时标题不能为空'
      } else if (innerText.length > 40) {
        this.$message.warning('课时标题不能大于40个字', 3)
        this.step.courseList[index].name = innerText.substring(0, 40)
        this.step.courseList[index].editable = false
      } else {
        this.step.courseList[index].name = innerText
        this.step.courseList[index].editable = false
      }
    },
    datadragEnd (evt) {
      evt.preventDefault()
      // console.log('拖动前的索引 :' + evt.oldIndex)
      // console.log('拖动后的索引 :' + evt.newIndex)
    },
    handleDelete (index) {
      const _this = this
      this.$confirm({
        content: '确实要删除这个视频吗?',
        onOk () {
          // 从s3上删除相应的视频资源
          const objectName = _this.step.courseList[index].url
          removeObject(objectName).then((res) => {
            // const res = response.data
            if (res.status === 'success') {
              _this.$message.success('从s3删除视频成功~', 3)
            } else {
              _this.$message.warning('从s3删除视频失败，请联系管理员~', 3)
            }
          }).catch((err) => {
            _this.$message.warning('从s3删除视频失败，请联系管理员~' + err, 10)
          })
          _this.step.courseList.splice(index, 1)
        },
        onCancel () {}
      })
    },
    nextStep () {
      const that = this
      const { $store, step } = this
      that.loading = true
      that.timer = setTimeout(function () {
        that.loading = false
        $store.dispatch({
          type: 'form/submitStepForm',
          payload: { ...step }
        })
        that.$emit('nextStep')
      }, 1500)
    },
    prevStep () {
      this.$emit('prevStep')
    }
  },
  beforeDestroy () {
    clearTimeout(this.timer)
  }
}
</script>

<style lang="less" scoped>
  .stepFormText {
    margin-bottom: 24px;

    .ant-form-item-label,
    .ant-form-item-control {
      line-height: 22px;
    }
  }
  .test{
      border:1px solid #ccc;
  }
  .drag-hint {
    width:400px;
    height:28px;
    display:block;
    margin: 20px;
    color: #1faeff;
  }
  .drag-name {
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space:nowrap;
    min-width: 20px;
    width:300px;
    height:28px;
    display:inline-block;
  }
  .drag-button {
    float: right;
    margin-right: 10px;
  }
  .drag-item{
      width: 500px;
      height: 40px;
      line-height: 40px;
      margin: auto;
      position: relative;
      margin-top:10px;
      padding-left: 10px;
      border: medium solid rgb(20, 20, 20);
  }
  .drag-item:hover {
    cursor: move;
    border: medium dotted rgb(9, 199, 247);
  }
  .ghostClass{
      opacity: 1;
  }
  .bottom{
      width: 200px;
      height: 50px;
      position: relative;
      background: blue;
      top:2px;
      left: 2px;
      transition: all .5s linear;
  }
  span[contenteditable]:focus {
    cursor: text;
    color: rgb(248, 125, 10) ;
  }
</style>
