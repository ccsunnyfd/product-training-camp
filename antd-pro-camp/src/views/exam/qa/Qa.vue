<template>
  <div>
    <!-- 功能卡片头 -->
    <a-card>
      <a-icon type="bars" />
      <span>题目列表</span>
      <a-button
        style="float: right;"
        @click="handleAddQuestion"
        size="default">
        新增题目
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
        <span slot="qType" slot-scope="text, record">
          {{ qTypes[record.qType-1] }}
        </span>
        <span slot="action" slot-scope="text, record">
          <a-button
            size="small"
            type="primary"
            @click="handleShowQuestion(record.id)">查看
          </a-button>
          <a-divider type="vertical" />
          <a-button
            size="small"
            type="primary"
            @click="handleEditQuestion(record.id)">修改
          </a-button>
          <a-divider type="vertical" />
          <a-dropdown>
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="handleDeleteQuestion(record.id)"><a-icon type="delete" />删除</a-menu-item>
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
    <a-modal title="题目查看" v-model="showDetail" :closable="false" :mask-closable="false">
      <div>
        <span style="font-weight: bold;">题目标题: </span>
        <span>{{ question.title }}</span>
      </div>
      <div>
        <span style="font-weight: bold;">题目类型：</span>
        <span>{{ qTypes[question.qType-1] }}</span>
      </div>
      <div>
        <span style="font-weight: bold;">选项: </span>
        <div v-for="(o, index) in question.optionAndRight.optionList" :key="index">
          <span>{{ "(" + String.fromCharCode('A'.charCodeAt() + index) + ") " + o.text }}</span>
          <a-icon v-if="question.optionAndRight.rightIds.indexOf(o.id) !== -1" type="check" style="color: green;" />
        </div>
      </div>
      <div>
        <span style="font-weight: bold;">分值：</span>
        <span>{{ question.points }}</span>
      </div>
      <div>
        <span style="font-weight: bold;">答案解析: </span>
        <span>{{ question.analysis }}</span>
      </div>
      <div slot="footer">
        <a-button type="primary" @click="showDetail = false">确定</a-button>
      </div>
    </a-modal>
    <!-- 新建和修改对话框 -->
    <a-modal title="问题编辑" v-model="showEdit" :closable="false" :mask-closable="false">
      <!-- <Form ref="question" :model="question" :rules="ruleValidate"> -->
      <a-form :form="form">
        <a-form-item>
          <a-input
            v-decorator="[
              'id',
              {
                initialValue: question.id
              }
            ]"
            type="hidden"
            style="position: absolute">
          </a-input>
        </a-form-item>
        <a-form-item label="题目类型：">
          <a-select
            v-decorator="[
              'qType',
              {
                initialValue: question.qType,
                rules: [{ required: true, message: '必须选择题目类型' }]}
            ]"
            @change="handleQtypeChange"
            placeholder="题目类型">
            <a-select-option v-for="(d,index) in qTypes" :value="index+1" :key="d">{{ d }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="分值：">
          <a-select
            v-decorator="[
              'points',
              {
                initialValue: question.points,
                rules: [{ required: true, message: '必须选择题目分值' }]}
            ]"
            placeholder="分值">
            <a-select-option v-for="d in pointTypes" :value="d" :key="d">{{ d }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="题目：">
          <a-input
            v-decorator="[
              'title',
              {
                initialValue: question.title,
                rules: [{ required: true, message: '必须填写题目问题' }]}
            ]"
            type="textarea"
            row="2"
            placeholder="题目"
          ></a-input>
        </a-form-item>
        <a-form-item label="选项：">
          <option-list
            :type="question.qType"
            v-decorator="[
              'optionAndRight',
              {
                initialValue: { optionList: question.optionAndRight.optionList, rightIds: question.optionAndRight.rightIds },
                rules: [{ validator: checkOptions }]
              }
            ]"
          />
        </a-form-item>
        <a-form-item label="答案解析：">
          <a-input
            v-decorator="[
              'analysis',
              {
                initialValue: question.analysis
              }
            ]"
            type="textarea"
            row="1"
            placeholder="答案解析"
          ></a-input>
        </a-form-item>
      </a-form>
      <div slot="footer">
        <a-button @click="cancelQuestion">取消</a-button>
        <a-button type="primary" :loading="saveLoading" @click="saveQuestion">保存</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { saveOrUpdateQuestion, getQuestionList, getQuestionById, removeQuestionById } from '@/api/data.js'
import OptionList from '@/components/OptionList/OptionList'

const columns = [
  {
    title: '题目标题',
    dataIndex: 'title',
    // sorter: true,
    width: '20%'
  },
  {
    title: '类型',
    dataIndex: 'qType',
    scopedSlots: { customRender: 'qType' }
  },
  {
    title: '分值',
    dataIndex: 'points'
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]

export default {
  name: 'Qa',
  mounted () {
    this.fetch({
      page: 1,
      size: 10
    })
  },
  components: {
    OptionList
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
      pointTypes: [2, 3, 5, 10, 20],
      question: {
        points: 10,
        title: '',
        analysis: '',
        optionAndRight: {
          optionList: [],
          rightIds: []
        }
      }
    }
  },
  methods: {
    handleQtypeChange (value) {
      this.question.qType = value
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
    // 显示某个题目信息的展示对话框
    handleShowQuestion (id) {
      getQuestionById({
        id: id
      }).then(res => {
        if (res.status === 'success') {
          this.question = res.data
          this.showDetail = true
        }
      })
    },
    // 新增一条题目信息的对话框
    handleAddQuestion () {
      this.question = {
        title: '',
        analysis: '',
        optionAndRight: {
          optionList: [],
          rightIds: []
        }
      }
      this.form.resetFields()
      this.showEdit = true
    },
    // 修改某个题目信息的对话框
    handleEditQuestion (id) {
      getQuestionById({
        id: id
      }).then(res => {
        if (res.status === 'success') {
          this.question = res.data
          this.form.resetFields()
          this.showEdit = true
        }
      })
    },
    // 删除某个题目信息
    handleDeleteQuestion (id) {
      removeQuestionById(id)
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
      getQuestionList({
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
    cancelQuestion () {
      this.showEdit = false
    },
    // 对选项列表的校验规则
    checkOptions (rule, value, callback) {
      let status = true
      const options = value.optionList
      const rightIds = value.rightIds
      const countRight = rightIds.length
      for (let i = 0; i < options.length; i = i + 1) {
        if (!options[i].text || options[i].text.trim() === '') {
          status = false
          break
        }
      }
      if (this.question.qType === 3 && countRight < 2) {
        status = false
      } else if (countRight < 1) {
        status = false
      }
      if (status) {
        callback()
      } else {
        // eslint-disable-next-line standard/no-callback-literal
        callback('所有选项都不能为空且多选题至少2个答案单选判断至少1个答案')
      }
    },
    // 保存题目
    saveQuestion () {
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log(values)
          saveOrUpdateQuestion(values).then(res => {
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
    }
  }
}
</script>
