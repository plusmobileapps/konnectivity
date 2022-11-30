//
//  MutableStateBuilder.swift
//  iosApp
//
//  Created by Andrew Steinmetz on 11/30/22.
//  Copyright © 2022 plus mobile apps. All rights reserved.
//

import shared

func valueOf<T: AnyObject>(_ value: T) -> Value<T> {
    return MutableValueBuilderKt.MutableValue(initialValue: value) as! MutableValue<T>
}
