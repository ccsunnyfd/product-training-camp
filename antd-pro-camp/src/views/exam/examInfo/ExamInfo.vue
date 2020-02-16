<template>
  <div>
    <!-- 功能卡片头 -->
    <a-card>
      <a-icon type="bars" />
      <span>考试列表</span>
      <a-button
        style="float: right;"
        @click="handleAddTest"
        size="default">
        新增考试
      </a-button>
    </a-card>
    <!-- 存量数据列表 -->
    <div>
      <a-table
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="data"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <span slot="shuffle" slot-scope="text, record">
          {{ record? '是': '否' }}
        </span>
        <span slot="action" slot-scope="text, record">
          <a-button
            size="small"
            type="primary"
            @click="handleShowTest(record.id)">查看
          </a-button>
          <a-divider type="vertical" />
          <a-button
            size="small"
            type="primary"
            @click="handleEditTest(record.id)">修改
          </a-button>
          <a-divider type="vertical" />
          <a-dropdown>
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="handleDeleteTest(record.id)"><a-icon type="delete" />删除</a-menu-item>
            </a-menu>
            <a-button
              size="small"
              type="danger"
            >
              更多 <a-icon type="down" />
            </a-button>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- 查看对话框 -->
    <a-modal title="查看考试" v-model="showDetail" :closable="false" :mask-closable="false">
      <div>
        <span>考试标题: </span>
        <span>{{ test.title }}</span>
      </div>
      <div>
        <span>考试时长（分钟）: </span>
        <span>{{ test.timeLimit }}</span>
      </div>
      <div>
        <span>题序随机: </span>
        <span>{{ test.shuffle? '是': '否' }}</span>
      </div>
      <div>
        <span>总分: </span>
        <span>{{ test.totalScore }}</span>
      </div>
      <a-table
        :columns="bindCol"
        :rowKey="record => record.id"
        :dataSource="test.questionList"
        :pagination="false"
        size="small"
        :border="true"
      >
        <span slot="qType" slot-scope="text, record">
          {{ qTypes[record.qType-1] }}
        </span>
        <span slot="optionList" slot-scope="text, record">
          <div v-for="(o,i) in record.optionAndRight.optionList" :key="o.id">
            {{ '(' + String.fromCharCode("A".charCodeAt() + i) + ') ' + o.text }}
          </div>
        </span>
      </a-table>
      <div slot="footer">
        <a-button type="primary" @click="showDetail = false">确定</a-button>
      </div>
    </a-modal>
    <!-- 新建和修改对话框 -->
    <a-modal title="编辑考试" v-model="showEdit" :closable="false" :mask-closable="false">
      <a-form :form="form">
        <a-row :gutter="8">
          <a-col :span="5">
            <a-form-item>
              <a-input
                v-decorator="[
                  'id',
                  {
                    initialValue: test.id
                  }
                ]"
                type="hidden"
                style="position: absolute">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item>
              <a-input
                v-decorator="[
                  'questionList',
                  {
                    initialValue: JSON.stringify(test.questionList),
                    rules: [{ validator: checkQuestionList }]
                  }
                ]"
                type="hidden"
                style="position: absolute">
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="8">
          <a-col :span="16">
            <a-form-item label="考试标题：">
              <a-input
                v-decorator="[
                  'title',
                  {
                    initialValue: test.title,
                    rules: [{ required: true, message: '必须填写考试标题' }]
                  }
                ]"
                type="textarea"
                row="2"
                placeholder="考试标题"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="5">
            <a-form-item label="当前试卷总分：">
              <a-input
                v-decorator="[
                  'totalScore',
                  {
                    initialValue: test.totalScore,
                    rules: [{ required: true, message: '总分不为空' }]
                  }
                ]"
                disabled
                type="text">
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="8">
          <a-col :span="8">
            <a-form-item label="考试时长：">
              <a-select
                v-decorator="[
                  'timeLimit',
                  {
                    initialValue: test.timeLimit,
                    rules: [{ required: true, message: '必须选择考试时长' }]
                  }
                ]"
                @change="handleSelectLimitTimeChange"
              >
                <a-select-option value="10">
                  10
                </a-select-option>
                <a-select-option value="15">
                  15
                </a-select-option>
                <a-select-option value="20">
                  20
                </a-select-option>
                <a-select-option value="25">
                  25
                </a-select-option>
                <a-select-option value="30">
                  30
                </a-select-option>
                <a-select-option value="35">
                  35
                </a-select-option>
                <a-select-option value="40">
                  40
                </a-select-option>
                <a-select-option value="45">
                  45
                </a-select-option>
                <a-select-option value="50">
                  50
                </a-select-option>
                <a-select-option value="55">
                  55
                </a-select-option>
                <a-select-option value="60">
                  60
                </a-select-option>
              </a-select>
              分钟
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item>
              <a-checkbox
                v-decorator="[
                  'shuffle',
                  {
                    initialValue: test.shuffle
                  }
                ]"
              >
                是否随机题序（防作弊）
              </a-checkbox>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="8">
          <a-col :span="16">
            <div>
              <div>选择关联模板组合题目</div>
              <a-button type="dashed" @click="ranTestTemp(3, 4, 3)">模板(3判断/4单选/3多选)</a-button>
              <a-button type="dashed" @click="ranTestTemp(6, 7, 7)">模板(6判断/7单选/7多选)</a-button>
            </div>
          </a-col>
        </a-row></a-form>
      <div slot="footer">
        <a-button @click="cancelTest">取消</a-button>
        <a-button type="primary" :loading="saveLoading" @click="saveTest">保存</a-button>
      </div>
      <!-- 待绑定题目列表 -->
      <a-table
        :columns="bindCol"
        :rowKey="record => record.id"
        :dataSource="test.questionList"
        :pagination="false"
        size="small"
        :border="true"
      >
        <span slot="qType" slot-scope="text, record">
          {{ qTypes[record.qType-1] }}
        </span>
        <span slot="optionList" slot-scope="text, record">
          <div v-for="(o,i) in record.optionAndRight.optionList" :key="o.id">
            {{ '(' + String.fromCharCode("A".charCodeAt() + i) + ') ' + o.text }}
          </div>
        </span>
      </a-table>
    </a-modal>
  </div>
</template>

<script>
import { saveOrUpdateTest, getTestList, getTestById, removeTestById, getSampleListByQtype } from '@/api/data.js'

const columns = [
  {
    title: '考试标题',
    dataIndex: 'title',
    // sorter: true,
    width: '20%'
  },
  {
    title: '考试时长(分钟)',
    dataIndex: 'timeLimit',
    width: '10%'
  },
  {
    title: '随机题序',
    dataIndex: 'shuffle',
    width: '10%',
    scopedSlots: { customRender: 'shuffle' }
  },
  {
    title: '总分',
    dataIndex: 'totalScore',
    // sorter: true,
    width: '10%'
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]

const bindCol = [
  {
    title: '题目标题',
    dataIndex: 'title',
    width: '35%'
  },
  {
    title: '类型',
    dataIndex: 'qType',
    width: '10%',
    scopedSlots: { customRender: 'qType' }
  },
  {
    title: '分值',
    dataIndex: 'points',
    width: '10%'
  },
  {
    title: '选项',
    key: 'optionList',
    scopedSlots: { customRender: 'optionList' }
  }
]

export default {
  name: 'ExamInfo',
  mounted () {
    this.fetch({
      page: 1,
      size: 10
    })
  },
  data () {
    return {
      form: this.$form.createForm(this),
      showDetail: false,
      showEdit: false,
      saveLoading: false,
      pagination: {},
      loading: false,
      columns,
      data: [],
      qTypes: ['判断题', '单选题', '多选题'],
      test: {
        title: '',
        timeLimit: 10,
        shuffle: true,
        totalScore: 0,
        questionList: []
      },
      bindCol
    }
  },
  methods: {
    // 考试时长选择改变时修改对应的form
    handleSelectLimitTimeChange (value) {
      this.form.setFieldsValue({
        timeLimit: value
      })
    },
    handleTableChange (pagination, filters, sorter) {
      const pager = { ...this.pagination }
      pager.current = pagination.current
      this.pagination = pager
      this.fetch({
        keywords: '',
        page: pagination.current,
        size: pagination.pageSize
        // sortField: sorter.field,
        // sortOrder: sorter.order,
        // ...filters
      })
    },
    // 显示某个考试信息的展示对话框
    handleShowTest (id) {
      getTestById({
        id: id
      }).then(res => {
        if (res.status === 'success') {
          this.test = res.data
          this.showDetail = true
        }
      })
    },
    // 新增一条考试信息的对话框
    handleAddTest () {
      this.test = {
        title: '',
        timeLimit: 10,
        shuffle: true,
        totalScore: 0,
        qustionList: []
      }
      this.form.resetFields()
      this.showEdit = true
    },
    // 修改某个考试信息的对话框
    handleEditTest (id) {
      getTestById({
        id: id
      }).then(res => {
        if (res.status === 'success') {
          this.test = res.data
          this.form.resetFields()
          this.showEdit = true
        }
      })
    },
    // 删除某个考试信息
    handleDeleteTest (id) {
      removeTestById(id)
        .then(resp => {
          if (resp.status === 'success') {
            this.$message.success(resp.msg, 2)
            this.fetch({
              keywords: '',
              page: this.pagination.current,
              size: this.pagination.pageSize
            })
          } else {
            this.$message.warning(resp.msg, 2)
          }
        })
    },
    fetch (params = {}) {
      const page = params.page
      const size = params.size
      this.loading = true
      getTestList({
        keywords: '',
        page: page,
        size: size
      }).then(res => {
        if (res.status === 'success') {
          const pagination = { ...this.pagination }
          // Read total count from server
          pagination.total = res.totalCount
          this.pagination = pagination
          this.data = res.data
        }
        this.loading = false
      })
    },
    cancelTest () {
      this.showEdit = false
    },
    // 对题目列表的校验规则
    checkQuestionList (rule, value, callback) {
      const jsonArray = JSON.parse(value)
      const status = jsonArray instanceof Array && jsonArray.length > 0
      if (status) {
        callback()
      } else {
        // eslint-disable-next-line standard/no-callback-literal
        callback('题目列表不能为空')
      }
    },
    // 保存考试
    saveTest () {
      this.form.validateFields((err, values) => {
        values.questionList = JSON.parse(values.questionList)
        if (!err) {
          saveOrUpdateTest(values).then(res => {
            if (res.status === 'success') {
              this.fetch({
                keywords: '',
                page: this.pagination.current,
                size: this.pagination.pageSize
              })
            }
          })
          this.showEdit = false
        }
      })
    },
    ranTestTemp (samplesize1, samplesize2, samplesize3) {
      const qType1 = 1
      const qType2 = 2
      const qType3 = 3
      Promise.all([
        // 获取判断题
        getSampleListByQtype(qType1, samplesize1),
        // 获取单选题
        getSampleListByQtype(qType2, samplesize2),
        // 获取多选题
        getSampleListByQtype(qType3, samplesize3)
      ]).then((results) => {
        this.test.questionList = [
          ...results[0].sampleList,
          ...results[1].sampleList,
          ...results[2].sampleList
        ]
        this.test.totalScore = 0
        results.forEach(element => {
          this.test.totalScore += element.totalScore
        })
        this.form.setFieldsValue({
          questionList: JSON.stringify(this.test.questionList),
          totalScore: this.test.totalScore
        })
      })
    }
  }
}
</script>
