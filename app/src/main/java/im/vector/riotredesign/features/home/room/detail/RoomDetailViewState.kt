/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotredesign.features.home.room.detail

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import im.vector.matrix.android.api.session.room.model.ReadReceipt
import im.vector.matrix.android.api.session.room.model.RoomSummary
import im.vector.matrix.android.api.session.room.timeline.TimelineData

data class RoomDetailViewState(
        val roomId: String,
        val eventId: String?,
        val readReceiptsForEventId: Map<String, List<ReadReceipt>> = emptyMap(),
        val asyncRoomSummary: Async<RoomSummary> = Uninitialized,
        val asyncTimelineData: Async<TimelineData> = Uninitialized
) : MvRxState {

    constructor(args: RoomDetailArgs) : this(roomId = args.roomId, eventId = args.eventId)

}