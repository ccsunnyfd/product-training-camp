/**
 * components util
 */
import { removeFile } from '@/api/data.js'
/**
 * 清理空值，对象
 * @param children
 * @returns {*[]}
 */
export function filterEmpty (children = []) {
  return children.filter(c => c.tag || (c.text && c.text.trim() !== ''))
}

/**
 * 获取字符串长度，英文字符 长度1，中文字符长度2
 * @param {*} str
 */
export const getStrFullLength = (str = '') =>
  str.split('').reduce((pre, cur) => {
    const charCode = cur.charCodeAt(0)
    if (charCode >= 0 && charCode <= 128) {
      return pre + 1
    }
    return pre + 2
  }, 0)

/**
 * 截取字符串，根据 maxLength 截取后返回
 * @param {*} str
 * @param {*} maxLength
 */
export const cutStrByFullLength = (str = '', maxLength) => {
  let showLength = 0
  return str.split('').reduce((pre, cur) => {
    const charCode = cur.charCodeAt(0)
    if (charCode >= 0 && charCode <= 128) {
      showLength += 1
    } else {
      showLength += 2
    }
    if (showLength <= maxLength) {
      return pre + cur
    }
    return pre
  }, '')
}

/**
 * 过滤url,并从oss上删除对象
 * @param {*} str
 */
export const delS3Object = (s3Url, val) => {
  // 过滤掉不是s3图片服务器地址的外域的url
  const s3Urlreg = new RegExp(s3Url)
  const serverUrlreg = /:\/\/(.*?)\/(.*)/
  // 过滤掉并发起图片删除请求
  if (s3Urlreg.test(val)) {
    const objectName = serverUrlreg.exec(val)[2]
    // 从s3上删除相应的图片资源
    removeFile(objectName)
      .then(res => {
        if (res.status === 'success') {
          this.$message.success('从s3删除对象成功~', 3)
        } else {
          this.$message.warning('从s3删除对象失败，请联系管理员~', 3)
        }
      })
      .catch(err => {
        this.$message.warning('从s3删除对象失败，请联系管理员~' + err, 10)
      })
  }
}

/**
 * 过滤url数组，筛选出oss的url
 * @param {*} array
 */
export const filterS3UrlArray = (s3Url, array) => {
  // 过滤掉不是s3图片服务器地址的外域的url
  const s3Urlreg = new RegExp(s3Url)
  const serverUrlreg = /:\/\/(.*?)\/(.*)/
  // 过滤掉并发起图片删除请求
  const urlNeedDelete = array.reduce((finalList, url) => {
    if (s3Urlreg.test(url)) {
      finalList.push(serverUrlreg.exec(url)[2])
    }
    return finalList
  }, [])
  return urlNeedDelete
}

/**
 * 从文本字符串中提取出url,返回url数组
 * @param {*} str
 */
export const getImgUrlArray = (val) => {
  const reg = /<img\b.*?(?:>|\/>)/gi // 全局匹配<img >的子串
  const regSrc = /\bsrc\b\s*=\s*['"]?([^'"]*)['"]?/i // 匹配子串中的url部分
  const editorImgUrl = [] // 编辑框内剩余的图片url
  // const matchList = localStorage.editorCache.match(reg) // 全局匹配<img >的子串列表
  const matchList = val.match(reg) // 全局匹配<img >的子串列表
  for (const index in matchList) {
    const srcStr = matchList[index].match(regSrc)
    if (srcStr.length > 0) {
      editorImgUrl.push(srcStr[1])
    }
  }
  return editorImgUrl
}
