//
//  ObservableValue.swift
//  iosApp
//
//  Created by Andrew Steinmetz on 11/30/22.
//  Copyright © 2022 plus mobile apps. All rights reserved.
//

import shared

public class ObservableValue<T : AnyObject> : ObservableObject {
    private let observableValue: Value<T>

    @Published
    var value: T
    
    private var observer: ((T) -> Void)?
    
    init(_ value: Value<T>) {
        self.observableValue = value
        self.value = observableValue.value
        self.observer = { [weak self] value in self?.value = value }

        observableValue.subscribe(observer: observer!)
    }
    
    deinit {
        self.observableValue.unsubscribe(observer: self.observer!)
    }
}
