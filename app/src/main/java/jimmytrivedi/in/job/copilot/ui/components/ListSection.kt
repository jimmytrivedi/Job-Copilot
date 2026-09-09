package jimmytrivedi.`in`.job.copilot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ListSection(
    title: String,
    items: List<String>,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
