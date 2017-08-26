package com.whf.changtheme.skin.attr;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.whf.changtheme.skin.config.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WHF on 2017/3/1.
 */

public class SkinAttrSupport {

    /**
     * 返回当前View所有需要换肤的属性集
     * @param attrs
     * @param context
     * @return List<SkinAttr<resName,resType>>
     */
    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context) {

        List<SkinAttr> skinAttrList = new ArrayList<>();
        SkinAttrType attrType = null;
        SkinAttr skinAttr = null;

        for (int i = 0, n = attrs.getAttributeCount(); i < n; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (attrValue.startsWith("@")) {

                int id = -1;

                try {
                    id = Integer.parseInt(attrValue.substring(1));
                } catch (NumberFormatException e) {
                    continue;
                }

                String resName = context.getResources().getResourceEntryName(id);
                if (resName.startsWith(Constants.SKIN_PREFIX)) {
                    attrType = getSupportAttrType(attrName);
                    if (attrType == null) {
                        continue;
                    } else {
                        skinAttr = new SkinAttr(resName, attrType);
                        skinAttrList.add(skinAttr);
                    }
                }
            }
        }
        return skinAttrList;
    }

    /**
     * 根据属性名返回属性类型
     * @param attrName
     * @return
     */
    private static SkinAttrType getSupportAttrType(String attrName) {
        for (SkinAttrType attrType : SkinAttrType.values()) {
            if (attrType.getResType().equals(attrName)) {
                return attrType;
            }
        }
        return null;
    }
}
