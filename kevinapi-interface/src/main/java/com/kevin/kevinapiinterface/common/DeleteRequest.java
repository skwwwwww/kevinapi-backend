package com.kevin.kevinapiinterface.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author kevin
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}