/*
 * Copyright 2012 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.asgard

import com.netflix.asgard.mock.Mocks
import spock.lang.Specification

class ConfigServiceSpec extends Specification {

    ConfigService configService = new ConfigService(grailsApplication: Mocks.grailsApplication())

    def 'should return true for m3 instance EBS check'(){
        expect:
        configService.instanceTypeNeedsEbsVolumes('m3.') == true
        configService.getSizeOfEbsVolumesAddedToLaunchConfigs() == 125
        configService.getEbsVolumeDeviceNamesForLaunchConfigs() == ['/dev/sdb', '/dev/sdc']
    }

    def 'should return correct excluded launch permissions for mass delete'() {
        expect:
        configService.getExcludedLaunchPermissionsForMassDelete() == [Mocks.SEG_AWS_ACCOUNT_ID] as Set
    }

    def 'cachedUserDataMaxLength should be huge by default'() {
        expect:
        configService.getCachedUserDataMaxLength() == Integer.MAX_VALUE
    }

    def 'cachedUserDataMaxLength should be at least zero'() {
        when:
        ConfigService configService = new ConfigService(grailsApplication: [
                config: [cloud: [cachedUserDataMaxLength: -5]]
        ])
        then:
        configService.getCachedUserDataMaxLength() == 0
    }

    def 'cachedUserDataMaxLength can be overridden'() {
        when:
        ConfigService configService = new ConfigService(grailsApplication: [
                config: [cloud: [cachedUserDataMaxLength: 20]]
        ])
        then:
        configService.getCachedUserDataMaxLength() == 20
    }
}
