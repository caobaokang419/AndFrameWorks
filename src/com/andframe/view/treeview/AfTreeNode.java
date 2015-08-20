package com.andframe.view.treeview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AfTreeNode<T> {
	public T value = null;// 该节点的值
	public int level = 0;// 该节点的值
	public boolean isExpanded = false;// 该节点是否展开
	public List<AfTreeNode<T>> children = null;
	
	protected AfTreeNode(T model) {
		this.value = model;
	}
	
	protected AfTreeNode(AfTreeNode<T> parent, T model, boolean isExpanded) {
		this.value = model;
		this.level = parent.level + 1;
		this.isExpanded = isExpanded;
	}
	
	public AfTreeNode(Collection<T> collect,AfTreeEstablishable<T> able,boolean isExpanded){
		this.level = -1;
		this.isExpanded = true;
		this.children = new ArrayList<AfTreeNode<T>>();
		List<T> list = new ArrayList<T>(collect);
		for (T model : collect) {
			if(able.isTopNode(model)){
				list.remove(model);
				children.add(new AfTreeNode<T>(this,model,isExpanded));
			}
		}
		for (AfTreeNode<T> child : children) {
			child.bulidChildNode(list,able,isExpanded);
		}
	}
	
	public AfTreeNode(Collection<T> collect, AfTreeNodeable<T> able,boolean isExpanded) {
		this.level = -1;
		this.isExpanded = true;
		this.children = new ArrayList<AfTreeNode<T>>();
		for (T model : collect) {
			children.add(new AfTreeNode<T>(this,model,able,isExpanded));
		}
	}

	protected AfTreeNode(AfTreeNode<T> parent, T model, AfTreeNodeable<T> able, boolean isExpanded) {
		this(parent,model,isExpanded);
		Collection<T> children = able.getChildren(model);
		if(children != null){
			this.children = new ArrayList<AfTreeNode<T>>();
			for (T child : children) {
				this.children.add(new AfTreeNode<T>(this,child,able,isExpanded));
			}
		}
	}

	protected void bulidChildNode(List<T> list, AfTreeEstablishable<T> able,boolean isExpanded) {
		children = new ArrayList<AfTreeNode<T>>();
		List<T> tlist = new ArrayList<T>(list);
		for (T model : tlist) {
			if(able.isChildOf(model,value)){
				children.add(new AfTreeNode<T>(this,model,isExpanded));
				list.remove(model);
			}
		}
		if(children.size() > 0){
			for (AfTreeNode<T> child : children) {
				child.bulidChildNode(list,able,isExpanded);
			}
		}else{
			children = null;
		}
	}
}
