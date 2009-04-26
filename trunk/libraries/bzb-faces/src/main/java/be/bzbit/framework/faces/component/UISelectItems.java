/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.bzbit.framework.faces.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

/**
 * This SelectItems tag does not require a collection of select items, you
 * can use a collection of objects, which will then be automatically
 * wrapped in SelectItems
 * 
 * @author Cagatay-Mert
 * @author Jurgen Lust
 */
public class UISelectItems extends javax.faces.component.UISelectItems {

    public static final String COMPONENT_TYPE_EX = "be.bzbit.framework.faces.UISelectItems";
    public static final String COMPONENT_FAMILY_EX = "be.bzbit.framework.faces.UISelectItems";
    private String var;
    private Object itemLabel;
    private Object itemValue;

    public UISelectItems() {
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY_EX;
    }

    public String getVar() {
        if (var != null) {
            return var;
        }
        ValueExpression vb = getValueExpression("var");
        String v = vb != null ? (String) vb.getValue(getFacesContext().getELContext()) : null;
        return v;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public Object getItemLabel() {
        if (itemLabel != null) {
            return itemLabel;
        }
        ValueExpression vb = getValueExpression("itemLabel");
        Object v = vb != null ? vb.getValue(getFacesContext().getELContext()) : null;
        return v;
    }

    public void setItemLabel(Object itemLabel) {
        this.itemLabel = itemLabel;
    }

    public Object getItemValue() {
        if (itemValue != null) {
            return itemValue;
        }
        ValueExpression vb = getValueExpression("itemValue");
        Object v = vb != null ? vb.getValue(getFacesContext().getELContext()) : null;
        return v;
    }

    public void setItemValue(Object itemValue) {
        this.itemValue = itemValue;
    }

    @Override
    public Object getValue() {
        Object value = super.getValue();
        return createSelectItems(value);
    }

    private SelectItem[] createSelectItems(Object value) {
        List items = new ArrayList();

        if (value instanceof SelectItem[]) {
            return (SelectItem[]) value;
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            for (Iterator iter = collection.iterator(); iter.hasNext();) {
                Object currentItem = (Object) iter.next();
                if (currentItem instanceof SelectItemGroup) {
                    SelectItemGroup itemGroup = (SelectItemGroup) currentItem;
                    SelectItem[] itemsFromGroup = itemGroup.getSelectItems();
                    for (int i = 0; i < itemsFromGroup.length; i++) {
                        items.add(itemsFromGroup[i]);
                    }
                } else {
                    putIteratorToRequestParam(currentItem);
                    SelectItem selectItem = createSelectItem();
                    removeIteratorFromRequestParam();
                    items.add(selectItem);
                }
            }
        } else if (value instanceof Map) {
            Map map = (Map) value;
            for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
                Entry currentItem = (Entry) iter.next();
                putIteratorToRequestParam(currentItem.getValue());
                SelectItem selectItem = createSelectItem();
                removeIteratorFromRequestParam();
                items.add(selectItem);
            }
        }

        return (SelectItem[]) items.toArray(new SelectItem[0]);
    }

    private SelectItem createSelectItem() {
        Object value = getItemValue();
        String label = getItemLabel() != null ? getItemLabel().toString() : null;
        SelectItem item = new SelectItem(value, label);
        return item;
    }

    private void putIteratorToRequestParam(Object object) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(getVar(), object);
    }

    private void removeIteratorFromRequestParam() {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(getVar());
    }

    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = var;
        values[2] = itemLabel;
        values[3] = itemValue;
        return ((Object) (values));
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        var = (String) values[1];
        itemLabel = values[2];
        itemValue = values[3];
    }
}

