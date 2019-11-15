package com.productcamp.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Course
 *
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(columnList = "productId")})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "course_gen")
    @GenericGenerator(name = "course_gen", strategy = "native")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "videoTrail", nullable = false, length = 1000)
    private String videoTrail;

    @Column(name = "chapterNum", nullable = false)
    private Long chapterNum;

    @Column(name = "productId", nullable = false)
    private Long productId;

//    视频封面的图片网络资源地址
    @Column(name = "poster", insertable = false, columnDefinition = "varchar(1000) default 'https://172.16.70.2:9000/product-camp/test/favicon/BCM.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=ZWHVQOKYOVTXSU9WH3CW%2F20190901%2F%2Fs3%2Faws4_request&X-Amz-Date=20190901T111711Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=d80417e01f3857dc1b15edaebb06c8aedd374d8ef4fdecf2a0e166fe1b605738'")
    private String poster;

    public Course(String title, String videoTrail, Long chapterNum, Long productId, String poster) {
        this.title = title;
        this.videoTrail = videoTrail;
        this.chapterNum = chapterNum;
        this.productId = productId;
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoTrail='" + videoTrail + '\'' +
                ", chapterNum=" + chapterNum +
                ", productId=" + productId +
                ", poster='" + poster + '\'' +
                '}';
    }
}
