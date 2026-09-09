package jimmytrivedi.`in`.job.copilot.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun BulletCard(text: String, modifier: Modifier = Modifier) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )
            IconButton(
                onClick = {
                    clipboard.setText(AnnotatedString(text))
                    Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = "Copy bullet to clipboard",
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
