//
//  BlocHolder.swift
//  iosApp
//
//  Created by Andrew Steinmetz on 11/30/22.
//  Copyright © 2022 plus mobile apps. All rights reserved.
//

import shared

class BlocHolder<T> {
    let lifecycle: LifecycleRegistry
    let bloc: T
    
    init(factory: (ComponentContext) -> T) {
        let lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let bloc = factory(DefaultComponentContext(lifecycle: lifecycle))
        self.lifecycle = lifecycle
        self.bloc = bloc
        
        lifecycle.onCreate()
    }
    
    deinit {
        lifecycle.onDestroy()
    }
}
