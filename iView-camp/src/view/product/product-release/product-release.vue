<template>
  <Card>
    <!-- 步骤条start -->
    <Steps class="margin-left-80" :current="currentStep">
      <Step
        v-for="item in stepList"
        :key="item.key"
        :title="item.title"
        :content="item.content"
      ></Step>
    </Steps>
    <!-- 步骤条end -->

    <!-- basicInfo表单start -->
    <i-form
      v-show="currentStep === 0"
      class="margin-top-40 padding-40"
      ref="basicInfo"
      :model="basicInfo"
      :rules="basicInfoRule"
      :label-width="130"
    >
      <Form-item label="产品名称" prop="name">
        <i-input
          v-model="basicInfo.name"
          placeholder="不少于2个字，最多20个字"
        ></i-input>
      </Form-item>
      <Form-item label="产品简介" prop="description">
        <i-input
          v-model="basicInfo.description"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 5 }"
          placeholder="字数不超过250字"
        ></i-input>
      </Form-item>
      <Form-item label="适用场景和人群" prop="scenario">
        <i-input
          v-model="basicInfo.scenario"
          type="textarea"
          :autosize="{
            minRows: 2,
            maxRows: 5,
          }"
          placeholder="字数不超过250字"
        ></i-input>
      </Form-item>
      <Row>
        <i-col span="8">
          <!-- 首页产品图标 -->
          <Form-item label="首页产品图标" prop="favicon">
            <Cavatar
              :fileinfo="basicInfo.favicon"
              :crop-width="200"
              :crop-height="200"
              @imgUpdate="handleFaviconUpdate"
            ></Cavatar>
          </Form-item>
        </i-col>
        <i-col span="8">
          <Form-item label="产品详情页图片" prop="prodImg">
            <Cavatar
              :fileinfo="basicInfo.prodImg"
              :crop-width="300"
              :crop-height="200"
              @imgUpdate="handleProdImgUpdate"
            ></Cavatar>
          </Form-item>
        </i-col>
        <i-col span="8">
          <Form-item label="左侧划栏的图标" prop="iconType">
            <CchooseIcon
              :iconinfo="basicInfo.iconType"
              @iconUpdate="handleIconUpdate"
            ></CchooseIcon>
          </Form-item>
        </i-col>
      </Row>
    </i-form>
    <!-- basicInfo表单end -->

    <!-- example富文本编辑start -->
    <i-form
      v-show="true"
      class="margin-top-40 padding-40"
      ref="applicationEx"
      :model="example"
      :rules="exampleRule"
      :label-width="130"
    >
      <Form-item label="应用案例标题" prop="title">
        <i-input v-model="example.title" placeholder="最多40个字"></i-input>
      </Form-item>
      <Form-item label="案例内容" prop="htmlContent">
        <div id="editor">
          <editor
            ref="editor"
            :value="example.htmlContent"
            @on-change="handleChange"
          />
        </div>
      </Form-item>
    </i-form>
    <!-- example富文本编辑end -->

    <!-- courseUpload课程上传start -->
    <i-form
      v-show="true"
      class="margin-top-40 padding-40"
      ref="courseUpload"
      :model="course"
      :rules="courseRule"
      :label-width="130"
    >
      <FormItem>
        <Row>
          <i-col span="12">
            <Button type="dashed" long @click="handleAddCourse" icon="md-add"
              >添加课时
            </Button>
          </i-col>
        </Row>
      </FormItem>
      <FormItem
        v-for="(item, index) in courseList"
        :key="item.title"
        +
        index
        label="课时标题"
        :prop="'items.' + index + '.value'"
        :rules="{
          required: true,
          message: '课时标题不能为空',
          trigger: 'blur',
        }"
      >
        <Row>
          <i-col span="18">
            <Input type="text" v-model="item.title" placeholder="课时标题..." />
          </i-col>
          <i-col span="18">
            <UploadVideo :edit-video="[]" />
            <!-- <Input type="text" v-model="item.videoTrail" placeholder="视频..." /> -->
          </i-col>
          <i-col span="4" offset="1">
            <Button @click="handleRemoveCourse(index)">Delete</Button>
          </i-col>
        </Row>
      </FormItem>
      <!-- <Form-item label="课程标题" prop="title">
        <i-input v-model="course.title" placeholder="最多40个字"></i-input>
      </Form-item> -->
      <Form-item label="案例内容" prop="videoTrail">
        <div id="uploadVideo">
          <UploadVideo :edit-video="[]" />
        </div>
      </Form-item>
    </i-form>
    <!-- courseUpload课程上传end -->

    <!-- 下一步及提交按钮start -->
    <div style="text-align: center;">
      <Button
        type="primary"
        :disabled="nextStepButton.disabled"
        @click="next"
        :loading="loading"
        >{{ nextStepButton.value }}
      </Button>
    </div>
    <!-- 下一步及提交按钮end -->
  </Card>
</template>

<script>
import Cavatar from '_c/cAvatar';
import CchooseIcon from '_c/cChooseIcon';
import Editor from '_c/editor';
import UploadVideo from '_c/upload-video';
import { submitBasicInfo, submitExample } from '@/api/data.js';
export default {
  name: 'ProductReleasePage',
  components: {
    Cavatar,
    CchooseIcon,
    Editor,
    UploadVideo,
  },
  data() {
    return {
      productId: 0, // 当前发布产品得id号
      currentStep: 0, // 当前操作步骤条的步骤号，从0开始
      loading: false, // 下一步/提交按钮上的loading效果
      nextStepButton: {
        disabled: false,
        value: '下一步',
      }, // 下一步/提交按钮上的使能和文字状态
      stepList: [
        {
          key: 'basicInfo',
          title: '进行中',
          content: '基本信息',
        },
        {
          key: 'applicationEx',
          title: '待进行',
          content: '应用实例',
        },
        {
          key: 'courseUpload',
          title: '待进行',
          content: '课程上传',
        },
        {
          key: 'examRelated',
          title: '待进行',
          content: '关联考试',
        },
      ], // 步骤条的内容
      basicInfo: {
        name: '',
        description: '',
        scenario: '',
        favicon: '',
        iconType: '',
        prodImg: '',
      }, // 基本信息的数据集
      basicInfoRule: {
        name: [
          { required: true, message: '请输入产品名称', trigger: 'blur' },
          { min: 2, message: '介绍不能少于2个字', trigger: 'blur' },
          { max: 20, message: '介绍不能多于20字', trigger: 'blur' },
        ],
        description: [
          { required: true, message: '请输入产品简介', trigger: 'blur' },
          { max: 250, message: '字数不超过250字', trigger: 'blur' },
        ],
        scenario: [
          { required: true, message: '请输入适用场景和人群', trigger: 'blur' },
          { max: 250, message: '字数不超过250字', trigger: 'blur' },
        ],
        favicon: [
          {
            required: true,
            message: '请选择首页图标',
            trigger: 'change',
          },
        ],
        iconType: [
          {
            required: true,
            message: '请选择侧滑栏的小图标',
            trigger: 'change',
          },
        ],
        prodImg: [
          {
            required: true,
            message: '请选择产品详情页图片',
            trigger: 'change',
          },
        ],
      }, // 基本信息的校验规则
      example: {
        title: '',
        htmlContent: '',
        productId: 0,
      }, // 应用案例的数据集
      exampleRule: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { max: 40, message: '标题不能多于40字', trigger: 'blur' },
        ],
        htmlContent: [
          { required: true, message: '请输入应用场景', trigger: 'blur' },
        ],
      }, // 应用案例的校验规则
      courseList: [
        {
          title: '',
          videoTrail: '',
          chapterNum: 0,
          productId: 0,
        },
      ], // 课程的数据集
      courseRule: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { max: 40, message: '标题不能多于40字', trigger: 'blur' },
        ],
        videoTrail: [
          { required: true, message: '请上传视频', trigger: 'blur' },
        ],
      }, // 应用案例的校验规则
    };
  },
  watch: {
    productId() {
      this.example.productId = this.productId;
    },
  },
  methods: {
    handleFaviconUpdate(url) {
      this.basicInfo.favicon = url;
    },
    handleProdImgUpdate(url) {
      this.basicInfo.prodImg = url;
    },
    handleIconUpdate(iconType) {
      this.basicInfo.iconType = iconType;
    },
    changeStepStatus() {
      this.stepList[this.currentStep].title = '已完成';
      if (this.currentStep === 2) {
        this.nextStepButton.value = '提交';
      } else if (this.currentStep === 3) {
        this.nextStepButton.disabled = true;
      }
      if (this.currentStep !== 3) {
        this.currentStep += 1;
        this.stepList[this.currentStep].title = '进行中';
      }
    },
    next() {
      this.handleSubmit(this.stepList[this.currentStep].key);
    },
    handleBasicInfoSubmit(basicInfo) {
      submitBasicInfo(basicInfo)
        .then(resp => {
          if (resp.data.status === 'success') {
            this.loading = false;
            this.$Message.success('提交成功~', 3);
            this.productId = resp.data.newId;
            this.changeStepStatus();
          } else {
            this.$Message.warning('提交失败，请联系管理员~', 3);
            this.loading = false;
          }
        })
        .catch(err => {
          this.$Message.warning('提交失败，请联系管理员~' + err, 5);
          this.loading = false;
        });
    },
    handleExampleSubmit(example) {
      // 提交前先从S3中删除已经在编辑器中delete的图片
      this.$refs.editor.removeDeletedImg();
      // 提交后要如果不是当前产品ID，要清空localStorage
      submitExample(example)
        .then(resp => {
          if (resp.data.status === 'success') {
            this.loading = false;
            this.$Message.success('提交成功~', 3);
            localStorage.clear();
            this.changeStepStatus();
          } else {
            this.$Message.warning('提交失败，请联系管理员~', 3);
            this.loading = false;
          }
        })
        .catch(err => {
          this.$Message.warning('提交失败，请联系管理员~' + err, 5);
          this.loading = false;
        });
    },
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.loading = true;
          if (name === 'basicInfo') {
            this.handleBasicInfoSubmit(this.basicInfo);
          } else if (name === 'applicationEx') {
            this.handleExampleSubmit(this.example);
          }
        } else {
          this.$Message.error('表单验证失败!', 3);
        }
      });
    },
    handleChange(html) {
      this.example.htmlContent = html;
    },
    // 添加课时
    handleAddCourse() {
      this.courseList.push({
        value: '',
        index: this.index,
        status: 1,
      });
    },
    // 移除课时
    handleRemove(index) {
      this.courseList.splice(index, 1);
    },
  },
  mounted() {
    //
  },
};
</script>

<style lang="less">
@import './product-release.less';
</style>
