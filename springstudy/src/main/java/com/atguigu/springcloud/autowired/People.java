package com.atguigu.springcloud.autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import javax.annotation.Resources;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/21 16:02
 */
public class People {

    // 如果现实定义了Autoired的required属性为false ，说明这个对象可以为null，否则不允许为空
    @Autowired
    private Cat cat;

    @Autowired
    private Dog dog;

    private String name;


    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "People{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
